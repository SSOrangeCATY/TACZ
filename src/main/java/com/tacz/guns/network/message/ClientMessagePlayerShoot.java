package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * 这里的 timestamp 应该是基于 base timestamp 的相对值
 */
public record ClientMessagePlayerShoot(long timestamp) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessagePlayerShoot> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_shoot"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerShoot> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            ClientMessagePlayerShoot::timestamp,
            ClientMessagePlayerShoot::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessagePlayerShoot data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player entity = context.player();
            IGunOperator.fromLivingEntity(entity).shoot(entity::getXRot, entity::getYRot, data.timestamp);
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }
}
