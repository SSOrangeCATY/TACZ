package com.tacz.guns.api.item.nbt;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.item.IAccessory;
import com.tacz.guns.api.item.component.AccessoryComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AccessoryItemDataAccessor extends IAccessory {
    String ATTACHMENT_ID_TAG = "AttachmentId";

    static int getZoomNumberFromItemStack(@Nullable ItemStack stack) {
        return stack != null ? stack.getOrDefault(AccessoryComponents.ZOOM_NUMBER,0) : 0;
    }

    @Override
    @Nonnull
    default ResourceLocation getAccessoryId(ItemStack accessoryStack) {
        return accessoryStack.getOrDefault(AccessoryComponents.ACCESSORY_ID, DefaultAssets.EMPTY_ATTACHMENT_ID);
    }

    @Override
    default void setAccessoryId(ItemStack accessoryStack, @Nullable ResourceLocation attachmentId) {
        if (attachmentId != null) {
            accessoryStack.set(AccessoryComponents.ACCESSORY_ID, attachmentId);
        } else {
            accessoryStack.remove(AccessoryComponents.ACCESSORY_ID);
        }
    }

    @Override
    @Nullable
    default ResourceLocation getSkinId(ItemStack accessoryStack) {
        return accessoryStack.get(AccessoryComponents.ACCESSORY_SKIN);
    }

    @Override
    default void setSkinId(ItemStack accessoryStack, @Nullable ResourceLocation skinId) {
        if (skinId != null) {
            accessoryStack.set(AccessoryComponents.ACCESSORY_SKIN, skinId);
        } else {
            accessoryStack.remove(AccessoryComponents.ACCESSORY_SKIN);
        }
    }

    @Override
    default int getZoomNumber(ItemStack accessoryStack) {
        return accessoryStack.getOrDefault(AccessoryComponents.ZOOM_NUMBER, 0);
    }

    @Override
    default void setZoomNumber(ItemStack accessoryStack, int zoomNumber) {
        accessoryStack.set(AccessoryComponents.ZOOM_NUMBER, zoomNumber);
    }

    @Override
    default boolean hasCustomLaserColor(ItemStack accessoryStack) {
        return accessoryStack.has(AccessoryComponents.LASER_COLOR);
    }

    @Override
    default int getLaserColor(ItemStack accessoryStack) {
        return accessoryStack.getOrDefault(AccessoryComponents.LASER_COLOR, 0xFF0000);
    }

    @Override
    default void setLaserColor(ItemStack accessoryStack, int color) {
        accessoryStack.set(AccessoryComponents.LASER_COLOR, color);
    }
    
}