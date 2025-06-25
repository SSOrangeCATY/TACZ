package com.tacz.guns.api.item;

import com.tacz.guns.api.item.accessory.AccessoryType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAccessory {
    /**
     * @return 如果物品类型为 IAttachment 则返回显式转换后的实例，否则返回 null。
     */
    @Nullable
    static IAccessory getIAttachmentOrNull(@Nullable ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.getItem() instanceof IAccessory iAttachment) {
            return iAttachment;
        }
        return null;
    }

    /**
     * 获取配件 ID
     */
    @Nonnull
    ResourceLocation getAccessoryId(ItemStack attachmentStack);

    /**
     * 设置配件 ID
     */
    void setAccessoryId(ItemStack attachmentStack, @Nullable ResourceLocation attachmentId);

    /**@deprecated
     */
    @Deprecated
    @Nullable
    ResourceLocation getSkinId(ItemStack attachmentStack);

    /**@deprecated
     */
    @Deprecated
    void setSkinId(ItemStack attachmentStack, @Nullable ResourceLocation skinId);

    /**
     * 获取瞄具配件的缩放倍率的数字索引，仅瞄具配件可用
     */
    int getZoomNumber(ItemStack attachmentStack);

    /**
     * 设置瞄具配件的缩放倍率的数字索引
     */
    void setZoomNumber(ItemStack attachmentStack, int zoomNumber);

    /**
     * 配件类型
     */
    @Nonnull
    AccessoryType getType(ItemStack attachmentStack);

    boolean hasCustomLaserColor(ItemStack attachmentStack);

    /**
     * 获取镭射配件的激光颜色
     * @return 镭射颜色，RGB
     */
    int getLaserColor(ItemStack attachmentStack);

    void setLaserColor(ItemStack attachmentStack, int color);
}
