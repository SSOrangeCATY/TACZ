package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerZoom implements CustomPacketPayload {
    public static final Type<ClientMessagePlayerZoom> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_zoom"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerZoom> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerZoom());

    public static void handle(ClientMessagePlayerZoom data, IPayloadContext context) {
        context.enqueueWork(() -> {
            IGunOperator.fromLivingEntity(context.player()).zoom();
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientMessagePlayerZoom;
    }
}
