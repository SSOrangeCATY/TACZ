package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.network.NetworkHandler;
import com.tacz.guns.resource.modifier.AttachmentPropertyManager;
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

public record ClientMessageRefitGun(int attachmentSlotIndex,int gunSlotIndex,AttachmentType attachmentType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageRefitGun> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_refit_gun"));

    public static final StreamCodec<ByteBuf, ClientMessageRefitGun> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientMessageRefitGun::attachmentSlotIndex,
            ByteBufCodecs.VAR_INT,
            ClientMessageRefitGun::gunSlotIndex,
            AttachmentType.STREAM_CODEC,
            ClientMessageRefitGun::attachmentType,
            ClientMessageRefitGun::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessageRefitGun data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Inventory inventory = player.getInventory();
            ItemStack attachmentItem = inventory.getItem(data.attachmentSlotIndex);
            ItemStack gunItem = inventory.getItem(data.gunSlotIndex);
            IGun iGun = IGun.getIGunOrNull(gunItem);
            if (iGun != null) {
                if (iGun.allowAttachment(gunItem, attachmentItem)) {
                    ItemStack oldAttachmentItem = iGun.getAttachment(gunItem, data.attachmentType);
                    iGun.installAttachment(gunItem, attachmentItem);
                    // 刷新配件数据
                    AttachmentPropertyManager.postChangeEvent(player, gunItem);
                    inventory.setItem(data.attachmentSlotIndex, oldAttachmentItem);
                    // 如果卸载的是扩容弹匣，吐出所有子弹
                    if (data.attachmentType == AttachmentType.EXTENDED_MAG) {
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
