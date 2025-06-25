package com.tacz.guns.item;

import com.tacz.guns.init.ModDataComponentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public enum GunTooltipPart {
    DESCRIPTION,
    AMMO_INFO,
    BASE_INFO,
    EXTRA_DAMAGE_INFO,
    UPGRADES_TIP,
    PACK_INFO;

    private final int mask = 1 << this.ordinal();

    public int getMask() {
        return this.mask;
    }

    public static int getHideFlags(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.DATA, CustomData.EMPTY).getUnsafe().getInt("hideFlags");
    }

    public static void setHideFlags(ItemStack stack, int mask) {
        stack.getOrDefault(ModDataComponentTypes.DATA, CustomData.EMPTY).getUnsafe().putInt("HideFlags", mask);
    }
}
