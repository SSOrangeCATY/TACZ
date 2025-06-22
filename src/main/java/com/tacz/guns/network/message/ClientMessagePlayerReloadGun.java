package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerReloadGun implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessagePlayerReloadGun> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_reload_gun"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerReloadGun> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerReloadGun());

    public static void handle(ClientMessagePlayerReloadGun data, IPayloadContext context) {
        context.enqueueWork(() -> {
            IGunOperator.fromLivingEntity(context.player()).reload();
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
