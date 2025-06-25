package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerCancelReload implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<ClientMessagePlayerCancelReload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_cancel_reload"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerCancelReload> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerCancelReload());

    public static void handle(ClientMessagePlayerCancelReload data, IPayloadContext context) {
        context.enqueueWork(() -> {
            IGunOperator.fromLivingEntity(context.player()).cancelReload();
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientMessagePlayerCancelReload;
    }
}
