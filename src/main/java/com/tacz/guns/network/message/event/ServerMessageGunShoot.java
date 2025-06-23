package com.tacz.guns.network.message.event;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.event.common.GunShootEvent;
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

public record ServerMessageGunShoot(
        int shooterId,
        ItemStack gunItemStack
) implements CustomPacketPayload {

    public static final Type<ServerMessageGunShoot> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_shoot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageGunShoot> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerMessageGunShoot::shooterId,
                    ItemStack.STREAM_CODEC,
                    ServerMessageGunShoot::gunItemStack,
                    ServerMessageGunShoot::new
            );

    public static void handle(ServerMessageGunShoot message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                if (level.getEntity(message.shooterId()) instanceof LivingEntity shooter) {
                    GunShootEvent gunShootEvent = new GunShootEvent(
                            shooter,
                            message.gunItemStack(),
                            context.flow().getReceptionSide().isClient()
                                    ? LogicalSide.CLIENT
                                    : LogicalSide.SERVER
                    );
                    NeoForge.EVENT_BUS.post(gunShootEvent);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}