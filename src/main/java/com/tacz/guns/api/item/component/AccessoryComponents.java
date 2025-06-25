package com.tacz.guns.api.item.component;

import com.mojang.serialization.Codec;
import com.tacz.guns.init.ModDataComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AccessoryComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ACCESSORY_ID =
            ModDataComponentTypes.COMPONENT_TYPES.register("accessory_id", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ACCESSORY_SKIN =
            ModDataComponentTypes.COMPONENT_TYPES.register("accessory_skin", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ZOOM_NUMBER =
            ModDataComponentTypes.COMPONENT_TYPES.register("accessory_zoom_number", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LASER_COLOR =
            ModDataComponentTypes.COMPONENT_TYPES.register("accessory_laser_color", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static void init(){}
}
