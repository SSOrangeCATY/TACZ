package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.inventory.GunSmithTableMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ClientMessageCraft(ResourceLocation recipeId,int menuId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageCraft> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_craft"));

    public static final StreamCodec<ByteBuf, ClientMessageCraft> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ClientMessageCraft::recipeId,
            ByteBufCodecs.VAR_INT,
            ClientMessageCraft::menuId,
            ClientMessageCraft::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessageCraft data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player entity = context.player();
            if (entity.containerMenu.containerId == data.menuId && entity.containerMenu instanceof GunSmithTableMenu menu) {
                menu.doCraft(data.recipeId, entity);
            }
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }
}
