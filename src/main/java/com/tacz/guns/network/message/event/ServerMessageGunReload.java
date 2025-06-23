package com.tacz.guns.network.message.event;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunReloadEvent;
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

public record ServerMessageGunReload(
        int shooterId,
        ItemStack gunItemStack
) implements CustomPacketPayload {

    public static final Type<ServerMessageGunReload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_reload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageGunReload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerMessageGunReload::shooterId,
                    ItemStack.STREAM_CODEC,
                    ServerMessageGunReload::gunItemStack,
                    ServerMessageGunReload::new
            );

    public static void handle(ServerMessageGunReload message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                if (level.getEntity(message.shooterId()) instanceof LivingEntity shooter) {
                    GunReloadEvent event = new GunReloadEvent(
                            shooter,
                            message.gunItemStack(),
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