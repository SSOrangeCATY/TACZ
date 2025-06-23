package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.client.resource.ClientIndexManager;
import com.tacz.guns.resource.network.CommonNetworkCache;
import com.tacz.guns.resource.network.DataType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record ServerMessageSyncGunPack(
        Map<DataType, Map<ResourceLocation, String>> cache
) implements CustomPacketPayload {
    public static final Type<ServerMessageSyncGunPack> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_sync_gun_pack"));

    public static final StreamCodec<ByteBuf, ServerMessageSyncGunPack> STREAM_CODEC =
            ByteBufCodecs.map(
                    (i)-> ((Map<DataType, Map<ResourceLocation, String>>) new HashMap<DataType, Map<ResourceLocation, String>>()),
                    DataType.STREAM_CODEC,
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.STRING_UTF8
                    )
            ).map(ServerMessageSyncGunPack::new, ServerMessageSyncGunPack::cache);

    public static void handle(ServerMessageSyncGunPack message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                CommonNetworkCache.INSTANCE.fromNetwork(message.cache());
                ClientIndexManager.reload();
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}