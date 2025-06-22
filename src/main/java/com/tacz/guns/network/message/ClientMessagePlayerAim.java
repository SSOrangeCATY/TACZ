package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.config.sync.SyncConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ClientMessagePlayerAim(boolean isAim) implements CustomPacketPayload {
    public static final Type<ClientMessagePlayerAim> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_aim"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerAim> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientMessagePlayerAim::isAim,
            ClientMessagePlayerAim::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessagePlayerAim data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player entity = context.player();
            if (!SyncConfig.ENABLE_CRAWL.get()) {
                return;
            }
            IGunOperator.fromLivingEntity(entity).aim(data.isAim());
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }
}
