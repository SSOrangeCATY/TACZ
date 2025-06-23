package com.tacz.guns.crafting.result;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.resource.pojo.data.block.TabConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GunSmithTableResult {
    public static final MapCodec<GunSmithTableResult> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("result").forGetter(GunSmithTableResult::getResult),
                    ResourceLocation.CODEC.optionalFieldOf("group", TabConfig.TAB_EMPTY).forGetter(GunSmithTableResult::getGroup),
                    RawGunTableResult.CODEC.optionalFieldOf("raw").forGetter(r -> Optional.ofNullable(r.raw))
            ).apply(instance, (result, group, rawOpt) ->
                    rawOpt.map(raw -> new GunSmithTableResult(raw, group))
                            .orElseGet(() -> new GunSmithTableResult(result, group))
            )
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GunSmithTableResult> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            GunSmithTableResult::getResult,
            ResourceLocation.STREAM_CODEC,
            GunSmithTableResult::getGroup,
            ByteBufCodecs.optional(RawGunTableResult.STREAM_CODEC),
            result -> Optional.ofNullable(result.raw),
             (resultStack, group, rawOpt) ->
                    rawOpt.map(rawGunTableResult -> new GunSmithTableResult(rawGunTableResult, group))
                            .orElseGet(() -> new GunSmithTableResult(resultStack, group))
    );

    public static final String GUN = "gun";
    public static final String AMMO = "ammo";
    public static final String ATTACHMENT = "attachment";
    public static final String CUSTOM = "custom";

    private ItemStack result = ItemStack.EMPTY;
    private ResourceLocation group = null;

    @Nullable
    private RawGunTableResult raw = null;

    public GunSmithTableResult(ItemStack result, @Nullable ResourceLocation group) {
        this.result = result;
        this.group = group==null ? TabConfig.TAB_EMPTY : group;
    }


    public GunSmithTableResult(@NotNull RawGunTableResult raw) {
        this.raw = raw;
    }

    public GunSmithTableResult(@NotNull RawGunTableResult raw, @Nullable ResourceLocation group) {
        this.raw = raw;
        this.group = group==null ? TabConfig.TAB_EMPTY : group;
    }

    public void init() {
        if (raw != null) {
            GunSmithTableResult result = RawGunTableResult.init(raw);
            this.result = result.getResult();
            if (group == null || group.equals(TabConfig.TAB_EMPTY)) {
                this.group = result.getGroup();
            }
            this.raw = null;
        }
    }

    public ItemStack getResult() {
        return result;
    }

    public ResourceLocation getGroup() {
        return group;
    }
}
