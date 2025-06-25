package com.tacz.guns.compat.jei;

import com.tacz.guns.api.item.*;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GunModSubtype {
    public static ISubtypeInterpreter<ItemStack> getAmmoSubtype() {
        return new ISubtypeInterpreter<>() {
            @Nullable
            public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context){
                return null;
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
                if (stack.getItem() instanceof IAmmo iAmmo) {
                    return iAmmo.getAmmoId(stack).toString();
                }
                return "";
            }
        };
    }

    public static ISubtypeInterpreter<ItemStack> getGunSubtype() {
        return new ISubtypeInterpreter<>() {
            @Nullable
            public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context){
                return null;
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(ItemStack stack, @NotNull UidContext context) {
                if (stack.getItem() instanceof IGun iGun) {
                    return iGun.getGunId(stack).toString();
                }
                return "";
            }
        };
    }

    public static ISubtypeInterpreter<ItemStack> getAttachmentSubtype() {
        return new ISubtypeInterpreter<>() {
            @Nullable
            public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context){
                return null;
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(ItemStack stack, @NotNull UidContext context) {
                if (stack.getItem() instanceof IAccessory iAttachment) {
                    return iAttachment.getAccessoryId(stack).toString();
                }
                return "";
            }
        };
    }

    public static ISubtypeInterpreter<ItemStack> getTableSubType() {
        return new ISubtypeInterpreter<>() {
            @Nullable
            public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context){
                return null;
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
                if (stack.getItem() instanceof IBlock iBlock) {
                    return iBlock.getBlockId(stack).toString();
                }
                return "";
            }
        };
    }


    public static ISubtypeInterpreter<ItemStack> getAmmoBoxSubtype() {
        return new ISubtypeInterpreter<>() {
            @Nullable
            public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context){
                return null;
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
                if (stack.getItem() instanceof IAmmoBox iAmmoBox) {
                    if (iAmmoBox.isAllTypeCreative(stack)) {
                        return "all_type_creative";
                    }
                    if (iAmmoBox.isCreative(stack)) {
                        return "creative";
                    }
                    return String.format("level_%d", iAmmoBox.getAmmoLevel(stack));
                }
                return "";
            }
        };
    }
}
