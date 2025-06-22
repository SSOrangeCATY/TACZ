package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerBoltGun implements CustomPacketPayload {
    public static final Type<ClientMessagePlayerBoltGun> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_bolt_gun"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerBoltGun> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerBoltGun());

    public static void handle(ClientMessagePlayerBoltGun data, IPayloadContext context) {
        context.enqueueWork(() -> {
            IGunOperator.fromLivingEntity(context.player()).bolt();
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
