package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;


public class ClientMessageSyncBaseTimestamp implements CustomPacketPayload {
    private static final Marker MARKER = MarkerManager.getMarker("SYNC_BASE_TIMESTAMP");

    public static final Type<ClientMessageSyncBaseTimestamp> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_sync_base_timestamp"));

    public static final StreamCodec<ByteBuf, ClientMessageSyncBaseTimestamp> STREAM_CODEC = StreamCodec.unit(new ClientMessageSyncBaseTimestamp());

    public static void handle(ClientMessageSyncBaseTimestamp data, IPayloadContext context) {
        context.enqueueWork(() -> {
            long timestamp = System.currentTimeMillis();
            ShooterDataHolder dataHolder = IGunOperator.fromLivingEntity(context.player()).getDataHolder();
            dataHolder.baseTimestamp = timestamp;
            GunMod.LOGGER.debug(MARKER, "Update server base timestamp: {}", dataHolder.baseTimestamp);
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
