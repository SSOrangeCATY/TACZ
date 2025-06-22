package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GunMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> DATA = TYPES.register("gun_data",
            ()-> new DataComponentType.Builder<CustomData>().persistent(CustomData.CODEC).build());
}
