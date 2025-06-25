package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.component.AccessoryComponents;
import com.tacz.guns.api.item.component.AmmoComponents;
import com.tacz.guns.api.item.component.BlockComponents;
import com.tacz.guns.api.item.component.GunComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GunMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> DATA = COMPONENT_TYPES.register("gun_data",
            ()-> new DataComponentType.Builder<CustomData>().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    static {
        GunMod.LOGGER.info("Data Component Types Registering...");
        GunComponents.init();
        AmmoComponents.init();
        AccessoryComponents.init();
        BlockComponents.init();
    }
}
