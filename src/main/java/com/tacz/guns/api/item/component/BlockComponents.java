package com.tacz.guns.api.item.component;

import com.tacz.guns.init.ModDataComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> BLOCK_ID =
            ModDataComponentTypes.COMPONENT_TYPES.register("block_id", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static void init(){}

}
