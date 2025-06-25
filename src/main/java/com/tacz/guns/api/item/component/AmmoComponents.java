package com.tacz.guns.api.item.component;

import com.mojang.serialization.Codec;
import com.tacz.guns.init.ModDataComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AmmoComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> AMMO_ID =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_id", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO_COUNT =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_count", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO_LEVEL =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_level", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> AMMO_CREATIVE =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_creative", () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> AMMO_ALL_TYPE =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_all_type_creative", () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO_DISPLAY_COLOR =
            ModDataComponentTypes.COMPONENT_TYPES.register("ammo_display_color", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static void init(){}

}
