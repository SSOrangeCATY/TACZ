package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.client.sound.SoundPlayManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerMessageSound(
        int entityId,
        ResourceLocation gunId,
        ResourceLocation gunDisplayId,
        String soundName,
        float volume,
        float pitch,
        int distance
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageSound> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_sound"));

    public static final StreamCodec<ByteBuf, ServerMessageSound> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, PartialMessage::entityId,
                    ResourceLocation.STREAM_CODEC, PartialMessage::gunId,
                    ResourceLocation.STREAM_CODEC, PartialMessage::gunDisplayId,
                    ByteBufCodecs.STRING_UTF8, PartialMessage::soundName,
                    ByteBufCodecs.FLOAT, PartialMessage::volume,
                    ByteBufCodecs.FLOAT, PartialMessage::pitch,
                    PartialMessage::new
            ),
            msg -> new PartialMessage(
                    msg.entityId(), msg.gunId(), msg.gunDisplayId(),
                    msg.soundName(), msg.volume(), msg.pitch()
            ),
            ByteBufCodecs.VAR_INT,
            ServerMessageSound::distance,
            PartialMessage::withDistance
    );

    public ServerMessageSound(int entityId, ResourceLocation gunId, String soundName, float volume, float pitch, int distance) {
        this(entityId, gunId, DefaultAssets.DEFAULT_GUN_DISPLAY_ID, soundName, volume, pitch, distance);
    }

    public static void handle(ServerMessageSound message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> SoundPlayManager.playMessageSound(message));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private record PartialMessage(
            int entityId,
            ResourceLocation gunId,
            ResourceLocation gunDisplayId,
            String soundName,
            float volume,
            float pitch
    ) {
        public ServerMessageSound withDistance(int distance) {
            return new ServerMessageSound(
                    entityId(), gunId(), gunDisplayId(),
                    soundName(), volume(), pitch(), distance
            );
        }
    }
}