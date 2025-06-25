package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerFireSelect implements CustomPacketPayload {
    public static final Type<ClientMessagePlayerFireSelect> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_fire_select"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerFireSelect> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerFireSelect());

    public static void handle(ClientMessagePlayerFireSelect data, IPayloadContext context) {
        context.enqueueWork(() -> {
            IGunOperator.fromLivingEntity(context.player()).fireSelect();
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
        return obj instanceof ClientMessagePlayerFireSelect;
    }
}
