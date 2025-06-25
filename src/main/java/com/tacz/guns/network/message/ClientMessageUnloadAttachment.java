package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.accessory.AccessoryType;
import com.tacz.guns.network.NetworkHandler;
import com.tacz.guns.resource.modifier.AccessoryPropertyManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;


public record ClientMessageUnloadAttachment(int gunSlotIndex, AccessoryType accessoryType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientMessageUnloadAttachment> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_unload_attachment"));

    public static final StreamCodec<ByteBuf, ClientMessageUnloadAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientMessageUnloadAttachment::gunSlotIndex,
            AccessoryType.STREAM_CODEC,
            ClientMessageUnloadAttachment::accessoryType,
            ClientMessageUnloadAttachment::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessageUnloadAttachment data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Inventory inventory = player.getInventory();
            ItemStack gunItem = inventory.getItem(data.gunSlotIndex);
            IGun iGun = IGun.getIGunOrNull(gunItem);
            if (iGun != null) {
                ItemStack accessoryItem = iGun.getAccessory(gunItem, data.accessoryType);
                if (!accessoryItem.isEmpty() && inventory.add(accessoryItem)) {
                    iGun.unloadAccessory(gunItem, data.accessoryType);
                    // 刷新配件数据
                    AccessoryPropertyManager.postChangeEvent(player, gunItem);
                    // 如果卸载的是扩容弹匣，吐出所有子弹
                    if (data.accessoryType == AccessoryType.EXTENDED_MAG) {
                        iGun.dropAllAmmo(player, gunItem);
                    }
                    player.inventoryMenu.broadcastChanges();
                    NetworkHandler.sendToClientPlayer(new ServerMessageRefreshRefitScreen(), player);
                }
            }
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }

}
