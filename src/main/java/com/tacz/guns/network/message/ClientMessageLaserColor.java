package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.IAccessory;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.accessory.AccessoryType;
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

import java.util.HashMap;
import java.util.Map;

public record ClientMessageLaserColor(Map<AccessoryType, Integer> colorMap, boolean applyGunColor, int gunColor, int gunSlotIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientMessageLaserColor> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "client_player_laser_color"));

    public static final StreamCodec<ByteBuf, ClientMessageLaserColor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    (i)-> new HashMap<>(),
                    AccessoryType.STREAM_CODEC,
                    ByteBufCodecs.VAR_INT
                    ),
            ClientMessageLaserColor::colorMap,
            ByteBufCodecs.BOOL,
            ClientMessageLaserColor::applyGunColor,
            ByteBufCodecs.VAR_INT,
            ClientMessageLaserColor::gunColor,
            ByteBufCodecs.VAR_INT,
            ClientMessageLaserColor::gunSlotIndex,
            ClientMessageLaserColor::new
    );

    public static ClientMessageLaserColor of(@NotNull ItemStack gun, int gunSlotIndex) {
        final Map<AccessoryType, Integer> cm = new HashMap<>();
        boolean agc = false;
        int gc = 0;
        int gsi = -1;

        if (gun.getItem() instanceof IGun iGun) {
            for (AccessoryType type : AccessoryType.values()) {
                ItemStack attachment = iGun.getAccessory(gun, type);
                if (attachment.getItem() instanceof IAccessory iAttachment) {
                    if (iAttachment.hasCustomLaserColor(attachment)) {
                        cm.put(type, iAttachment.getLaserColor(attachment));
                    }
                }
            }
            if (iGun.hasCustomLaserColor(gun)) {
                gc = iGun.getLaserColor(gun);
                agc = true;
            }
            gsi = gunSlotIndex;
        }
        return new ClientMessageLaserColor(cm, agc, gc, gsi);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMessageLaserColor data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Inventory inventory = player.getInventory();
            ItemStack gunItem = inventory.getItem(data.gunSlotIndex);
            IGun iGun = IGun.getIGunOrNull(gunItem);
            if (iGun != null) {
                for (var entry : data.colorMap.entrySet()) {
                    AccessoryType type = entry.getKey();
                    int color = entry.getValue();
                    ItemStack attachment = iGun.getAccessory(gunItem, type);
                    if (attachment.getItem() instanceof IAccessory iAttachment) {
                        iAttachment.setLaserColor(attachment, color);
                    }
                }
                if (data.applyGunColor) {
                    iGun.setLaserColor(gunItem, data.gunColor);
                }
            }
        }).exceptionally(e -> {
            GunMod.LOGGER.error("处理Payload失败", e);
            return null;
        });
    }



}
