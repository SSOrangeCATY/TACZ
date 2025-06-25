package com.tacz.guns.api.item.nbt;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.component.AmmoComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface AmmoBoxItemDataAccessor extends IAmmoBox {
    @Override
    default ResourceLocation getAmmoId(ItemStack ammoBox) {
        return ammoBox.getOrDefault(AmmoComponents.AMMO_ID, DefaultAssets.EMPTY_AMMO_ID);
    }

    @Override
    default void setAmmoId(ItemStack ammoBox, ResourceLocation ammoId) {
        ammoBox.set(AmmoComponents.AMMO_ID, ammoId);
    }

    @Override
    default int getAmmoCount(ItemStack ammoBox) {
        if (isAllTypeCreative(ammoBox) || isCreative(ammoBox)) {
            return Integer.MAX_VALUE;
        }
        return ammoBox.getOrDefault(AmmoComponents.AMMO_COUNT, 0);
    }

    @Override
    default void setAmmoCount(ItemStack ammoBox, int count) {
        if (isCreative(ammoBox)) {
            ammoBox.set(AmmoComponents.AMMO_COUNT, Integer.MAX_VALUE);
        } else {
            ammoBox.set(AmmoComponents.AMMO_COUNT, count);
        }
    }

    @Override
    default boolean isAmmoBoxOfGun(ItemStack gun, ItemStack ammoBox) {
        if (gun.getItem() instanceof IGun iGun && ammoBox.getItem() instanceof IAmmoBox iAmmoBox) {
            if (isAllTypeCreative(ammoBox)) {
                return true;
            }
            ResourceLocation ammoId = iAmmoBox.getAmmoId(ammoBox);
            if (ammoId.equals(DefaultAssets.EMPTY_AMMO_ID)) {
                return false;
            }
            ResourceLocation gunId = iGun.getGunId(gun);
            return TimelessAPI.getCommonGunIndex(gunId)
                    .map(gunIndex -> gunIndex.getGunData().getAmmoId().equals(ammoId))
                    .orElse(false);
        }
        return false;
    }

    @Override
    default ItemStack setAmmoLevel(ItemStack ammoBox, int level) {
        ammoBox.set(AmmoComponents.AMMO_LEVEL, Math.max(level, 0));
        return ammoBox;
    }

    @Override
    default int getAmmoLevel(ItemStack ammoBox) {
        return ammoBox.getOrDefault(AmmoComponents.AMMO_LEVEL, 0);
    }

    @Override
    default boolean isCreative(ItemStack ammoBox) {
        return ammoBox.getOrDefault(AmmoComponents.AMMO_CREATIVE, false);
    }

    @Override
    default boolean isAllTypeCreative(ItemStack ammoBox) {
        return ammoBox.getOrDefault(AmmoComponents.AMMO_ALL_TYPE, false);
    }

    @Override
    default ItemStack setCreative(ItemStack ammoBox, boolean isAllType) {
        if (isAllType) {
            ammoBox.set(AmmoComponents.AMMO_ALL_TYPE, true);
        } else {
            ammoBox.set(AmmoComponents.AMMO_ALL_TYPE,false);
        }
        ammoBox.set(AmmoComponents.AMMO_CREATIVE,true);
        return ammoBox;
    }
}
