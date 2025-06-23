package com.tacz.guns.network.message.event;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunDrawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerMessageGunDraw(
        int entityId,
        ItemStack previousGunItem,
        ItemStack currentGunItem
) implements CustomPacketPayload {

    public static final Type<ServerMessageGunDraw> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_draw"));

    // 主StreamCodec定义
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageGunDraw> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerMessageGunDraw::entityId,
                    ItemStack.STREAM_CODEC,
                    ServerMessageGunDraw::previousGunItem,
                    ItemStack.STREAM_CODEC,
                    ServerMessageGunDraw::currentGunItem,
                    ServerMessageGunDraw::new
            );

    public static void handle(ServerMessageGunDraw message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                if (level.getEntity(message.entityId()) instanceof LivingEntity livingEntity) {
                    GunDrawEvent event = new GunDrawEvent(
                            livingEntity,
                            message.previousGunItem(),
                            message.currentGunItem(),
                            context.flow().getReceptionSide().isClient()
                                    ? LogicalSide.CLIENT
                                    : LogicalSide.SERVER
                    );
                    NeoForge.EVENT_BUS.post(event);
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}