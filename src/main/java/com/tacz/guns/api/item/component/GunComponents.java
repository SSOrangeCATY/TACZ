package com.tacz.guns.api.item.component;

import com.mojang.serialization.Codec;
import com.tacz.guns.api.item.accessory.AccessoryMap;
import com.tacz.guns.init.ModDataComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class GunComponents {
    // 基础枪械信息
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> GUN_ID =
            ModDataComponentTypes.COMPONENT_TYPES.register("gun_id", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> DISPLAY_ID =
            ModDataComponentTypes.COMPONENT_TYPES.register("display_id", () -> DataComponentType.<ResourceLocation>builder()
                .persistent(ResourceLocation.CODEC)
                .networkSynchronized(ResourceLocation.STREAM_CODEC)
                .build());
    
    // 弹药系统
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CURRENT_AMMO = 
        ModDataComponentTypes.COMPONENT_TYPES.register("current_ammo", () -> DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BULLET_IN_BARREL = 
        ModDataComponentTypes.COMPONENT_TYPES.register("bullet_in_barrel", () -> DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DUMMY_AMMO = 
        ModDataComponentTypes.COMPONENT_TYPES.register("dummy_ammo", () -> DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_DUMMY_AMMO = 
        ModDataComponentTypes.COMPONENT_TYPES.register("max_dummy_ammo", () -> DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());
    
    // 射击模式
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> FIRE_MODE = 
        ModDataComponentTypes.COMPONENT_TYPES.register("fire_mode", () -> DataComponentType.<String>builder()
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            .build());
    
    // 配件系统
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ATTACHMENT_LOCK = 
        ModDataComponentTypes.COMPONENT_TYPES.register("attachment_lock", () -> DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .build());
    
    // 过热系统
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> HEAT_AMOUNT = 
        ModDataComponentTypes.COMPONENT_TYPES.register("heat_amount", () -> DataComponentType.<Float>builder()
            .persistent(Codec.FLOAT)
            .networkSynchronized(ByteBufCodecs.FLOAT)
            .build());
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> OVERHEAT_LOCK = 
        ModDataComponentTypes.COMPONENT_TYPES.register("overheat_lock", () -> DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .build());
    
    // 经验系统
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GUN_EXP = 
        ModDataComponentTypes.COMPONENT_TYPES.register("gun_exp", () -> DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());
    
    // 配件存储
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AccessoryMap>> ATTACHMENTS =
        ModDataComponentTypes.COMPONENT_TYPES.register("attachments", () -> DataComponentType.<AccessoryMap>builder()
            .persistent(AccessoryMap.CODEC)
            .networkSynchronized(AccessoryMap.STREAM_CODEC)
            .build());

    public static void init(){}

}