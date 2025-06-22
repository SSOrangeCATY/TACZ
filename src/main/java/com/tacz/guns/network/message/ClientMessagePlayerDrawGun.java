package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.entity.IGunOperator;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMessagePlayerDrawGun implements CustomPacketPayload {
    public static final Type<ClientMessagePlayerDrawGun> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_draw_gun"));

    public static final StreamCodec<ByteBuf, ClientMessagePlayerDrawGun> STREAM_CODEC = StreamCodec.unit(new ClientMessagePlayerDrawGun());

    public static void handle(ClientMessagePlayerDrawGun data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Inventory inventory = player.getInventory();
            int selected = inventory.selected;
            IGunOperator.fromLivingEntity(player).draw(() -> inventory.getItem(selected));
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
