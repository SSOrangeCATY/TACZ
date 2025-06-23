package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerMessageLevelUp(ItemStack gun,int level) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageLevelUp> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_player_levelup"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageLevelUp> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ServerMessageLevelUp::gun,
            ByteBufCodecs.VAR_INT,
            ServerMessageLevelUp::level,
            ServerMessageLevelUp::new
    );

    public static void handle(ServerMessageLevelUp data, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.enqueueWork(() -> onLevelUp(data));
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void onLevelUp(ServerMessageLevelUp message) {
        int level = message.getLevel();
        ItemStack gun = message.getGun();
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        // TODO 在完成了枪械升级逻辑后，解封下面的代码
                /*
                if (GunLevelManager.DAMAGE_UP_LEVELS.contains(level)) {
                    Minecraft.getInstance().getToasts().addToast(new GunLevelUpToast(gun,
                            Component.translatable("toast.tacz.level_up"),
                            Component.translatable("toast.tacz.sub.damage_up")));
                } else if (level >= GunLevelManager.MAX_LEVEL) {
                    Minecraft.getInstance().getToasts().addToast(new GunLevelUpToast(gun,
                            Component.translatable("toast.tacz.level_up"),
                            Component.translatable("toast.tacz.sub.final_level")));
                } else {
                    Minecraft.getInstance().getToasts().addToast(new GunLevelUpToast(gun,
                            Component.translatable("toast.tacz.level_up"),
                            Component.translatable("toast.tacz.sub.level_up")));
                }*/
    }

    public ItemStack getGun() {
        return this.gun;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
