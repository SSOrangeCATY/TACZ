package com.tacz.guns.util.datafixer;

import com.google.common.collect.ImmutableMap;
import com.tacz.guns.api.DefaultAssets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.tacz.guns.api.item.nbt.AccessoryItemDataAccessor.*;

public final class AttachmentIdFix {
    private AttachmentIdFix() {
    }

    public static final Map<ResourceLocation, ResourceLocation> OLD_TO_NEW;
    static  {
        OLD_TO_NEW = ImmutableMap.<ResourceLocation, ResourceLocation>builder()
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_knight_qd"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_knight_qd"))
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_mirage"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_mirage"))
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_phantom_s1"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_phantom_s1"))
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_ptilopsis"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_ptilopsis"))
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_ursus"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_ursus"))
                .put(ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silence_vulture"), ResourceLocation.fromNamespaceAndPath("tacz", "muzzle_silencer_vulture"))
                .build();
    }

    public static ResourceLocation updateAttachmentId(ResourceLocation old) {
        return OLD_TO_NEW.getOrDefault(old, old);
    }
}
