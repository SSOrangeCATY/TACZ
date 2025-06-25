package com.tacz.guns.api.item.nbt;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAccessory;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.accessory.AccessoryType;
import com.tacz.guns.api.item.accessory.AccessoryMap;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.component.AccessoryComponents;
import com.tacz.guns.api.item.component.GunComponents;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.client.resource.GunDisplayInstance;
import com.tacz.guns.client.resource.index.ClientAccessoryIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface GunItemDataAccessor extends IGun {
    String GUN_ID_TAG = "GunId";

    @Override
    default boolean useDummyAmmo(ItemStack gun) {
        return gun.has(GunComponents.DUMMY_AMMO);
    }

    @Override
    default int getDummyAmmoAmount(ItemStack gun) {
        return Math.max(0, gun.getOrDefault(GunComponents.DUMMY_AMMO, 0));
    }

    @Override
    default void setDummyAmmoAmount(ItemStack gun, int amount) {
        gun.set(GunComponents.DUMMY_AMMO, Math.max(amount, 0));
    }

    @Override
    default void addDummyAmmoAmount(ItemStack gun, int amount) {
        if (!useDummyAmmo(gun)) return;
        int maxDummyAmmo = hasMaxDummyAmmo(gun) ? getMaxDummyAmmoAmount(gun) : Integer.MAX_VALUE;
        int newAmount = Math.min(getDummyAmmoAmount(gun) + amount, maxDummyAmmo);
        setDummyAmmoAmount(gun, Math.max(newAmount, 0));
    }

    @Override
    default boolean hasMaxDummyAmmo(ItemStack gun) {
        return gun.has(GunComponents.MAX_DUMMY_AMMO);
    }

    @Override
    default int getMaxDummyAmmoAmount(ItemStack gun) {
        return Math.max(0, gun.getOrDefault(GunComponents.MAX_DUMMY_AMMO, 0));
    }

    @Override
    default void setMaxDummyAmmoAmount(ItemStack gun, int amount) {
        gun.set(GunComponents.MAX_DUMMY_AMMO, Math.max(amount, 0));
    }

    @Override
    default float getAimingZoom(ItemStack gunItem) {
        float zoom = 1;
        ResourceLocation scopeId = this.getAccessoryId(gunItem, AccessoryType.SCOPE);
        boolean builtin = false;
        if (scopeId.equals(DefaultAssets.EMPTY_ATTACHMENT_ID)) {
            scopeId = getBuiltInAccessoryId(gunItem, AccessoryType.SCOPE);
            builtin = true;
        }
        if (!DefaultAssets.isEmptyAttachmentId(scopeId)) {
            ItemStack scope = this.getAccessory(gunItem, AccessoryType.SCOPE);
            int zoomNumber = builtin ? 0 : AccessoryItemDataAccessor.getZoomNumberFromItemStack(scope);
            float[] zooms = TimelessAPI.getClientAttachmentIndex(scopeId).map(ClientAccessoryIndex::getZoom).orElse(null);
            if (zooms != null) {
                zoom = zooms[zoomNumber % zooms.length];
            }
        } else {
            zoom = TimelessAPI.getGunDisplay(gunItem).map(GunDisplayInstance::getIronZoom).orElse(1f);
        }
        return zoom;
    }

    @Override
    default boolean hasAttachmentLock(ItemStack gun) {
        return gun.getOrDefault(GunComponents.ATTACHMENT_LOCK, false);
    }

    @Override
    default void setAttachmentLock(ItemStack gun, boolean lock) {
        gun.set(GunComponents.ATTACHMENT_LOCK, lock);
    }

    @Override
    @Nonnull
    default ItemStack getAccessory(ItemStack gun, AccessoryType type) {
        if (!allowAccessoryType(gun, type)) {
            return ItemStack.EMPTY;
        }
        return gun.getOrDefault(GunComponents.ATTACHMENTS,new AccessoryMap()).get(type);
    }

    @Override
    @NotNull
    default ItemStack getBuiltinAccessory(ItemStack gun, AccessoryType type) {
        IGun iGun = IGun.getIGunOrNull(gun);
        if (iGun == null) {
            return ItemStack.EMPTY;
        }
        CommonGunIndex index = TimelessAPI.getCommonGunIndex(iGun.getGunId(gun)).orElse(null);
        if (index != null){
            var builtin = index.getGunData().getBuiltInAccessories();
            if (builtin.containsKey(type)) {
                return AttachmentItemBuilder.create().setId(builtin.get(type)).build();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    default ResourceLocation getBuiltInAccessoryId(ItemStack gun, AccessoryType type) {
        IGun iGun = IGun.getIGunOrNull(gun);
        if (iGun == null) {
            return DefaultAssets.EMPTY_ATTACHMENT_ID;
        }
        CommonGunIndex index = TimelessAPI.getCommonGunIndex(iGun.getGunId(gun)).orElse(null);
        if (index != null){
            var builtin = index.getGunData().getBuiltInAccessories();
            if (builtin.containsKey(type)) {
                return builtin.get(type);
            }
        }
        return DefaultAssets.EMPTY_ATTACHMENT_ID;
    }

    @Override
    @Nonnull
    default ResourceLocation getAccessoryId(ItemStack gun, AccessoryType type) {
        return gun.getOrDefault(AccessoryComponents.ACCESSORY_ID,DefaultAssets.EMPTY_ATTACHMENT_ID);
    }

    @Override
    @Nonnull
    default ResourceLocation getGunId(ItemStack gun) {
        return gun.getOrDefault(GunComponents.GUN_ID, DefaultAssets.EMPTY_GUN_ID);
    }

    @Override
    default void setGunId(ItemStack gun, @Nullable ResourceLocation gunId) {
        if (gunId != null) {
            gun.set(GunComponents.GUN_ID, gunId);
        } else {
            gun.remove(GunComponents.GUN_ID);
        }
    }

    @Override
    @NotNull
    default ResourceLocation getGunDisplayId(ItemStack gun) {
        return gun.getOrDefault(GunComponents.DISPLAY_ID, DefaultAssets.DEFAULT_GUN_DISPLAY_ID);
    }

    @Override
    default void setGunDisplayId(ItemStack gun, ResourceLocation displayId) {
        if (displayId != null) {
            gun.set(GunComponents.DISPLAY_ID, displayId);
        } else {
            gun.remove(GunComponents.DISPLAY_ID);
        }
    }

    @Override
    default int getLevel(ItemStack gun) {
        return getLevel(getExp(gun));
    }

    @Override
    default int getExp(ItemStack gun) {
        return gun.getOrDefault(GunComponents.GUN_EXP, 0);
    }

    @Override
    default int getExpToNextLevel(ItemStack gun) {
        int exp = getExp(gun);
        int level = getLevel(exp);
        if (level >= getMaxLevel()) {
            return 0;
        }
        int nextLevelExp = getExp(level + 1);
        return nextLevelExp - exp;
    }

    @Override
    default int getExpCurrentLevel(ItemStack gun) {
        int exp = getExp(gun);
        int level = getLevel(exp);
        if (level <= 0) {
            return exp;
        } else {
            return exp - getExp(level - 1);
        }
    }

    default void setExp(ItemStack gun, int exp) {
        gun.set(GunComponents.GUN_EXP, exp);
    }

    @Override
    default FireMode getFireMode(ItemStack gun) {
        String mode = gun.getOrDefault(GunComponents.FIRE_MODE, FireMode.UNKNOWN.name());
        try {
            return FireMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            return FireMode.UNKNOWN;
        }
    }

    @Override
    default void setFireMode(ItemStack gun, @Nullable FireMode fireMode) {
        gun.set(GunComponents.FIRE_MODE, (fireMode != null ? fireMode : FireMode.UNKNOWN).name());
    }

    @Override
    default int getCurrentAmmoCount(ItemStack gun) {
        return gun.getOrDefault(GunComponents.CURRENT_AMMO, 0);
    }

    @Override
    default void setCurrentAmmoCount(ItemStack gun, int ammoCount) {
        gun.set(GunComponents.CURRENT_AMMO, Math.max(ammoCount, 0));
    }

    @Override
    default void reduceCurrentAmmoCount(ItemStack gun) {
        if (!useInventoryAmmo(gun)) {
            setCurrentAmmoCount(gun, getCurrentAmmoCount(gun) - 1);
        }
    }

    @Override
    default void installAccessory(@Nonnull ItemStack gun, @Nonnull ItemStack attachment) {
        if (!allowAccessory(gun, attachment)) return;

        IAccessory iAttachment = IAccessory.getIAttachmentOrNull(attachment);
        if (iAttachment == null) return;

        AccessoryMap map = gun.getOrDefault(GunComponents.ATTACHMENTS, new AccessoryMap());
        map.put(iAttachment.getType(attachment), attachment.copy());
        gun.set(GunComponents.ATTACHMENTS, map);
    }

    @Override
    default void unloadAccessory(@Nonnull ItemStack gun, AccessoryType type) {
        if (!allowAccessoryType(gun, type)) return;

        AccessoryMap map = gun.getOrDefault(GunComponents.ATTACHMENTS, new AccessoryMap());
        map.remove(type);
        gun.set(GunComponents.ATTACHMENTS, map);
    }

    @Override
    default boolean hasBulletInBarrel(ItemStack gun) {
        return gun.getOrDefault(GunComponents.BULLET_IN_BARREL, false);
    }

    @Override
    default void setBulletInBarrel(ItemStack gun, boolean bulletInBarrel) {
        gun.set(GunComponents.BULLET_IN_BARREL, bulletInBarrel);
    }

    @Override
    default boolean hasCustomLaserColor(ItemStack gun) {
        return gun.has(AccessoryComponents.LASER_COLOR);
    }

    @Override
    default int getLaserColor(ItemStack gun) {
        return gun.getOrDefault(AccessoryComponents.LASER_COLOR, 0xFF0000);
    }

    @Override
    default void setLaserColor(ItemStack gun, int color) {
        gun.set(AccessoryComponents.LASER_COLOR, color);
    }

    @Override
    default boolean hasHeatData(ItemStack gun) {
        return gun.has(GunComponents.HEAT_AMOUNT);
    }

    @Override
    default boolean isOverheatLocked(ItemStack gun) {
        return gun.getOrDefault(GunComponents.OVERHEAT_LOCK, false);
    }

    @Override
    default void setOverheatLocked(ItemStack gun, boolean locked) {
        gun.set(GunComponents.OVERHEAT_LOCK, locked);
    }

    @Override
    default float getHeatAmount(ItemStack gun) {
        return gun.getOrDefault(GunComponents.HEAT_AMOUNT,0f);
    }

    @Override
    default void setHeatAmount(ItemStack gun, float amount) {
        gun.set(GunComponents.HEAT_AMOUNT, Math.max(amount, 0f));
    }

    @Override
    default float lerpRPM(ItemStack gun) {
        return TimelessAPI.getCommonGunIndex(getGunId(gun))
                .map(index -> index.getGunData().getHeatData())
                .map(heatData -> {
                    float heatPercentage = (getHeatAmount(gun) / heatData.getHeatMax());
                    return Mth.lerp(heatPercentage, heatData.getMinRpmMod(), heatData.getMaxRpmMod());
                }).orElse(1f);
    }

    @Override
    default float lerpInaccuracy(ItemStack gun) {
        return TimelessAPI.getCommonGunIndex(getGunId(gun))
                .map(index -> index.getGunData().getHeatData())
                .map(heatData -> {
                    float heatPercentage = (getHeatAmount(gun) / heatData.getHeatMax());
                    return Mth.lerp(heatPercentage, heatData.getMinInaccuracy(), heatData.getMaxInaccuracy());
                }).orElse(1f);
    }
}