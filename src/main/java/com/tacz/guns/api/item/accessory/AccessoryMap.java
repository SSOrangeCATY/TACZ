package com.tacz.guns.api.item.accessory;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public record AccessoryMap(Map<AccessoryType, ItemStack> attachments) {
    public static final Codec<AccessoryMap> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(AccessoryType.CODEC, ItemStack.CODEC)
                .fieldOf("attachments")
                .forGetter(AccessoryMap::attachments)
        ).apply(instance, AccessoryMap::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AccessoryMap> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            (i)-> Maps.newEnumMap(AccessoryType.class),
                            AccessoryType.STREAM_CODEC,
                            ItemStack.STREAM_CODEC
                    ),
                    AccessoryMap::attachments,
                    AccessoryMap::new
            );
    
    public AccessoryMap() {
        this(new EnumMap<>(AccessoryType.class));
    }
    
    public ItemStack get(AccessoryType type) {
        return attachments.getOrDefault(type, ItemStack.EMPTY);
    }
    
    public void put(AccessoryType type, ItemStack stack) {
        attachments.put(type, stack);
    }
    
    public void remove(AccessoryType type) {
        attachments.put(type,ItemStack.EMPTY);
    }
    
    public boolean contains(AccessoryType type) {
        return attachments.containsKey(type);
    }
}