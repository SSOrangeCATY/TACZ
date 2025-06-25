package com.tacz.guns.crafting.result;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.accessory.AccessoryType;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.resource.pojo.data.block.TabConfig;
import com.tacz.guns.resource.pojo.data.recipe.GunResult;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Optional;


/**
 * 配方加载时部分物品的上下文还未完成初始化<br/>
 * 等待到实际需要使用配方时再进行初始化
 */
public class RawGunTableResult {
    public static final Codec<RawGunTableResult> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(RawGunTableResult::getType),
                    Codec.INT.fieldOf("count").forGetter(RawGunTableResult::getCount),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(RawGunTableResult::getId),
                    GunResult.CODEC.optionalFieldOf("extraData").forGetter(r -> Optional.ofNullable(r.extraData)),
                    DataComponentMap.CODEC.optionalFieldOf("components").forGetter(r -> Optional.ofNullable(r.components))
            ).apply(instance, (type, count, id, extraDataOpt, nbtOpt) -> {
                RawGunTableResult result = new RawGunTableResult(type, id, count);
                extraDataOpt.ifPresent(result::setExtraData);
                nbtOpt.ifPresent(result::setComponents);
                return result;
            })
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RawGunTableResult> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            RawGunTableResult::getType,
            ByteBufCodecs.VAR_INT,
            RawGunTableResult::getCount,
            ResourceLocation.STREAM_CODEC,
            RawGunTableResult::getId,
            ByteBufCodecs.optional(GunResult.STREAM_CODEC),
            result -> Optional.ofNullable(result.extraData),
            ByteBufCodecs.optional(ByteBufCodecs.fromCodecWithRegistriesTrusted(DataComponentMap.CODEC)),
            result -> Optional.ofNullable(result.components),
            (type, count, id, extraDataOpt, nbtOpt) -> {
                RawGunTableResult result = new RawGunTableResult(type, id, count);
                extraDataOpt.ifPresent(result::setExtraData);
                nbtOpt.ifPresent(result::setComponents);
                return result;
            }
    );

    private final String type;
    private final int count;
    private final ResourceLocation id;
    @Nullable
    private GunResult extraData = null;
    @Nullable
    private DataComponentMap components = null;

    public RawGunTableResult(@NotNull String type, @NotNull ResourceLocation id, int count) {
        this.type = type;
        this.id = id;
        this.count = count;
    }

    public void setExtraData(@Nullable GunResult extraData) {
        this.extraData = extraData;
    }

    public void setComponents(@Nullable DataComponentMap components) {
        this.components = components;
    }

    public static GunSmithTableResult init(RawGunTableResult raw) {
        GunSmithTableResult result = switch (raw.type) {
            case GunSmithTableResult.GUN -> raw.getGunStack();
            case GunSmithTableResult.AMMO -> raw.getAmmoStack();
            case GunSmithTableResult.ATTACHMENT -> raw.getAttachmentStack();
            default -> new GunSmithTableResult(ItemStack.EMPTY, TabConfig.TAB_EMPTY);
        };

        if (raw.components != null && !result.getResult().isEmpty()) {
            DataComponentMap components = result.getResult().getComponents();
            components.forEach(component ->{
                component.applyTo((PatchedDataComponentMap) result.getResult().getComponents());
            });
        }
        return result;
    }

    private GunSmithTableResult getGunStack() {
        int ammoCount;
        EnumMap<AccessoryType, ResourceLocation> attachments;
        if (extraData != null) {
            ammoCount = Math.max(0, extraData.getAmmoCount());
            attachments = extraData.getAttachments();
        } else {
            ammoCount = 0;
            attachments = new EnumMap<>(AccessoryType.class);
        }

        return TimelessAPI.getCommonGunIndex(id).map(gunIndex -> {
            ItemStack itemStack = GunItemBuilder.create()
                    // TODO 占位检查
                    .setCount(1)
                    .setId(id)
                    .setAmmoCount(ammoCount)
                    .setAmmoInBarrel(false)
                    .putAllAttachment(attachments)
                    .setFireMode(gunIndex.getGunData().getFireModeSet().getFirst()).build();
            String raw = gunIndex.getType();
            if (!raw.contains(":")) {
                raw = GunMod.MOD_ID + ":" + raw;
            }
            ResourceLocation group = ResourceLocation.tryParse(raw);
            return new GunSmithTableResult(itemStack, group);
        }).orElse(new GunSmithTableResult(ItemStack.EMPTY, TabConfig.TAB_EMPTY));
    }

    private GunSmithTableResult getAmmoStack() {
        return new GunSmithTableResult(AmmoItemBuilder.create().setCount(count).setId(id).build(), TabConfig.TAB_AMMO);
    }

    private GunSmithTableResult getAttachmentStack() {
        return TimelessAPI.getCommonAttachmentIndex(id).map(attachmentIndex -> {
            ItemStack itemStack = AttachmentItemBuilder.create().setCount(count).setId(id).build();
            String raw = attachmentIndex.getType().name().toLowerCase(Locale.US);
            if (!raw.contains(":")) {
                raw = GunMod.MOD_ID + ":" + raw;
            }
            ResourceLocation group = ResourceLocation.tryParse(raw);
            return new GunSmithTableResult(itemStack, group);
        }).orElse(new GunSmithTableResult(ItemStack.EMPTY, TabConfig.TAB_EMPTY));
    }

    public @Nullable DataComponentMap getComponents() {
        return components;
    }

    public @Nullable GunResult getExtraData() {
        return extraData;
    }

    public int getCount() {
        return count;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
