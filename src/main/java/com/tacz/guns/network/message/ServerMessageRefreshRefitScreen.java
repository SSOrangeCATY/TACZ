package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.client.gui.GunRefitScreen;
import com.tacz.guns.resource.modifier.AccessoryPropertyManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ServerMessageRefreshRefitScreen implements CustomPacketPayload {
    public static final Type<ServerMessageRefreshRefitScreen> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_refresh_refit_screen"));

    public static final StreamCodec<ByteBuf, ServerMessageRefreshRefitScreen> STREAM_CODEC = StreamCodec.unit(new ServerMessageRefreshRefitScreen());

    public static void handle(ServerMessageRefreshRefitScreen data, IPayloadContext context) {
        context.enqueueWork((ServerMessageRefreshRefitScreen::updateScreen)).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    private static void updateScreen() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().screen instanceof GunRefitScreen screen) {
            screen.init();
            // 刷新配件数据，客户端的
            AccessoryPropertyManager.postChangeEvent(player, player.getMainHandItem());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ServerMessageRefreshRefitScreen;
    }
}
