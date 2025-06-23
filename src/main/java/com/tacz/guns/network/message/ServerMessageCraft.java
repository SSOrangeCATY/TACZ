package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.client.gui.GunSmithTableScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerMessageCraft(int menuId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageCraft> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_crafted"));

    public static final StreamCodec<ByteBuf, ServerMessageCraft> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerMessageCraft::menuId,
            ServerMessageCraft::new
    );

    public static void handle(ServerMessageCraft data, IPayloadContext context) {
        context.enqueueWork(() -> {
            updateScreen(data.menuId);
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void updateScreen(int containerId) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu.containerId == containerId && Minecraft.getInstance().screen instanceof GunSmithTableScreen screen) {
            screen.updateIngredientCount();
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
