package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.client.gameplay.IClientPlayerGunOperator;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ServerMessageSyncBaseTimestamp() implements CustomPacketPayload {
    private static final Marker MARKER = MarkerManager.getMarker("SYNC_BASE_TIMESTAMP");

    public static final Type<ServerMessageSyncBaseTimestamp> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_sync_base_timestamp"));

    public static final StreamCodec<ByteBuf, ServerMessageSyncBaseTimestamp> STREAM_CODEC =
            StreamCodec.unit(new ServerMessageSyncBaseTimestamp());

    public static void handle(ServerMessageSyncBaseTimestamp message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            long timestamp = System.currentTimeMillis();
            context.enqueueWork(() -> updateBaseTimestamp(timestamp));
            context.reply(new ClientMessageSyncBaseTimestamp());
        }
    }

    private static void updateBaseTimestamp(long timestamp) {
        LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);
        LocalPlayerDataHolder dataHolder = IClientPlayerGunOperator.fromLocalPlayer(player).getDataHolder();
        dataHolder.clientBaseTimestamp = timestamp;
        GunMod.LOGGER.debug(MARKER, "Update client base timestamp: {}", dataHolder.clientBaseTimestamp);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}