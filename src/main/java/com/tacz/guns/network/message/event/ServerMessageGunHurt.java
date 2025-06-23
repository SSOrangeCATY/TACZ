package com.tacz.guns.network.message.event;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
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

public record ServerMessageGunHurt(
        int bulletId,
        int hurtEntityId,
        int attackerId,
        ResourceLocation gunId,
        ResourceLocation gunDisplayId,
        float amount,
        boolean isHeadShot,
        float headshotMultiplier
) implements CustomPacketPayload {

    public static final Type<ServerMessageGunHurt> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_hurt"));

    // 内部记录类用于分组字段
    private record Part1(
            int bulletId,
            int hurtEntityId,
            int attackerId,
            ResourceLocation gunId
    ) {}

    private record Part2(
            ResourceLocation gunDisplayId,
            float amount,
            boolean isHeadShot,
            float headshotMultiplier
    ) {}

    private static final StreamCodec<RegistryFriendlyByteBuf, Part1> PART1_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, Part1::bulletId,
                    ByteBufCodecs.INT, Part1::hurtEntityId,
                    ByteBufCodecs.INT, Part1::attackerId,
                    ResourceLocation.STREAM_CODEC, Part1::gunId,
                    Part1::new
            );

    private static final StreamCodec<RegistryFriendlyByteBuf, Part2> PART2_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, Part2::gunDisplayId,
                    ByteBufCodecs.FLOAT, Part2::amount,
                    ByteBufCodecs.BOOL, Part2::isHeadShot,
                    ByteBufCodecs.FLOAT, Part2::headshotMultiplier,
                    Part2::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageGunHurt> STREAM_CODEC =
            StreamCodec.composite(
                    PART1_CODEC,
                    msg -> new Part1(msg.bulletId(), msg.hurtEntityId(), msg.attackerId(), msg.gunId()),
                    PART2_CODEC,
                    msg -> new Part2(msg.gunDisplayId(), msg.amount(), msg.isHeadShot(), msg.headshotMultiplier()),
                    (part1, part2) -> new ServerMessageGunHurt(
                            part1.bulletId(),
                            part1.hurtEntityId(),
                            part1.attackerId(),
                            part1.gunId(),
                            part2.gunDisplayId(),
                            part2.amount(),
                            part2.isHeadShot(),
                            part2.headshotMultiplier()
                    )
            );

    public static void handle(ServerMessageGunHurt message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                @Nullable Entity bullet = level.getEntity(message.bulletId());
                @Nullable Entity hurtEntity = level.getEntity(message.hurtEntityId());
                @Nullable LivingEntity attacker = level.getEntity(message.attackerId()) instanceof LivingEntity livingEntity ? livingEntity : null;

                NeoForge.EVENT_BUS.post(new EntityHurtByGunEvent.Post(
                        bullet,
                        hurtEntity,
                        attacker,
                        message.gunId(),
                        message.gunDisplayId(),
                        message.amount(),
                        null,
                        message.isHeadShot(),
                        message.headshotMultiplier(),
                        context.flow().getReceptionSide().isClient() ? LogicalSide.CLIENT : LogicalSide.SERVER
                ));
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}