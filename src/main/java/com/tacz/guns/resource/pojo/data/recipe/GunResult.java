package com.tacz.guns.resource.pojo.data.recipe;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.item.attachment.AttachmentType;
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
    private EnumMap<AttachmentType, ResourceLocation> attachments = Maps.newEnumMap(AttachmentType.class);

    public int getAmmoCount() {
        return ammoCount;
    }

    public EnumMap<AttachmentType, ResourceLocation> getAttachments() {
        return attachments;
    }

    public GunResult(){}

    public GunResult(int ammoCount, EnumMap<AttachmentType, ResourceLocation> attachments) {
        this.ammoCount = ammoCount;
        this.attachments = attachments;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, GunResult> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            GunResult::getAmmoCount,
            ByteBufCodecs.map(
                    (i)-> new EnumMap<>(AttachmentType.class),
                    AttachmentType.STREAM_CODEC,
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
                    Codec.unboundedMap(AttachmentType.CODEC, ResourceLocation.CODEC)
                            .optionalFieldOf("attachments", Maps.newEnumMap(AttachmentType.class))
                            .forGetter(g -> new HashMap<>(g.getAttachments()))
            ).apply(instance, (ammoCount, attachmentsMap) -> {
                EnumMap<AttachmentType, ResourceLocation> attachments = Maps.newEnumMap(AttachmentType.class);
                attachments.putAll(attachmentsMap);
                return new GunResult(ammoCount, attachments);
            })
    );
}
