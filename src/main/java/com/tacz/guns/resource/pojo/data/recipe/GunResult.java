package com.tacz.guns.resource.pojo.data.recipe;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.item.accessory.AccessoryType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.HashMap;

public class GunResult {
    @SerializedName("ammo_count")
    private int ammoCount = 0;

    @SerializedName("attachments")
    private EnumMap<AccessoryType, ResourceLocation> attachments = Maps.newEnumMap(AccessoryType.class);

    public int getAmmoCount() {
        return ammoCount;
    }

    public EnumMap<AccessoryType, ResourceLocation> getAttachments() {
        return attachments;
    }

    public GunResult(){}

    public GunResult(int ammoCount, EnumMap<AccessoryType, ResourceLocation> attachments) {
        this.ammoCount = ammoCount;
        this.attachments = attachments;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, GunResult> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            GunResult::getAmmoCount,
            ByteBufCodecs.map(
                    (i)-> new EnumMap<>(AccessoryType.class),
                    AccessoryType.STREAM_CODEC,
                    ResourceLocation.STREAM_CODEC
            ),
            GunResult::getAttachments,
            GunResult::new
    );

    public static final Codec<GunResult> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // ammoCount 字段，带有默认值 0
                    Codec.INT.optionalFieldOf("ammo_count", 0).forGetter(GunResult::getAmmoCount),
                    // attachments 字段，带有默认空 Map
                    Codec.unboundedMap(AccessoryType.CODEC, ResourceLocation.CODEC)
                            .optionalFieldOf("attachments", Maps.newEnumMap(AccessoryType.class))
                            .forGetter(g -> new HashMap<>(g.getAttachments()))
            ).apply(instance, (ammoCount, attachmentsMap) -> {
                EnumMap<AccessoryType, ResourceLocation> attachments = Maps.newEnumMap(AccessoryType.class);
                attachments.putAll(attachmentsMap);
                return new GunResult(ammoCount, attachments);
            })
    );
}
