package com.tacz.guns.network.message.event;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public record ServerMessageGunKill(
        int bulletId,
        int killEntityId,
        int attackerId,
        ResourceLocation gunId,
        ResourceLocation gunDisplayId,
        float baseDamage,
        boolean isHeadShot,
        float headshotMultiplier
) implements CustomPacketPayload {

    public static final Type<ServerMessageGunKill> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_kill"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Part1> PART1_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, Part1::bulletId,
                    ByteBufCodecs.INT, Part1::killEntityId,
                    ByteBufCodecs.INT, Part1::attackerId,
                    ResourceLocation.STREAM_CODEC, Part1::gunId,
                    Part1::new
            );

    private static final StreamCodec<RegistryFriendlyByteBuf, Part2> PART2_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, Part2::gunDisplayId,
                    ByteBufCodecs.FLOAT, Part2::baseDamage,
                    ByteBufCodecs.BOOL, Part2::isHeadShot,
                    ByteBufCodecs.FLOAT, Part2::headshotMultiplier,
                    Part2::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageGunKill> STREAM_CODEC =
            StreamCodec.composite(
                    PART1_CODEC,
                    msg -> new Part1(msg.bulletId(), msg.killEntityId(), msg.attackerId(), msg.gunId()),
                    PART2_CODEC,
                    msg -> new Part2(msg.gunDisplayId(), msg.baseDamage(), msg.isHeadShot(), msg.headshotMultiplier()),
                    (part1, part2) -> new ServerMessageGunKill(
                            part1.bulletId(),
                            part1.killEntityId(),
                            part1.attackerId(),
                            part1.gunId(),
                            part2.gunDisplayId(),
                            part2.baseDamage(),
                            part2.isHeadShot(),
                            part2.headshotMultiplier()
                    )
            );

    public static void handle(ServerMessageGunKill message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                @Nullable Entity bullet = level.getEntity(message.bulletId());
                @Nullable LivingEntity killedEntity = level.getEntity(message.killEntityId()) instanceof LivingEntity livingEntity ? livingEntity : null;
                @Nullable LivingEntity attacker = level.getEntity(message.attackerId()) instanceof LivingEntity livingEntity ? livingEntity : null;

                NeoForge.EVENT_BUS.post(new EntityKillByGunEvent(
                        bullet, killedEntity, attacker,
                        message.gunId(), message.gunDisplayId(),
                        message.baseDamage(), null,
                        message.isHeadShot(), message.headshotMultiplier(),
                        context.flow().getReceptionSide().isClient() ? LogicalSide.CLIENT : LogicalSide.SERVER
                ));
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private record Part1(
            int bulletId,
            int killEntityId,
            int attackerId,
            ResourceLocation gunId
    ) {}

    private record Part2(
            ResourceLocation gunDisplayId,
            float baseDamage,
            boolean isHeadShot,
            float headshotMultiplier
    ) {}
}