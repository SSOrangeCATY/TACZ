package com.tacz.guns.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record GunSmithTableIngredient(Ingredient ingredient, int count) {
    public static final Codec<GunSmithTableIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(GunSmithTableIngredient::ingredient),
                    Codec.INT.fieldOf("count").forGetter(GunSmithTableIngredient::count)
            ).apply(instance, GunSmithTableIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,GunSmithTableIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            GunSmithTableIngredient::ingredient,
            ByteBufCodecs.VAR_INT,
            GunSmithTableIngredient::count,
            GunSmithTableIngredient::new
    );
}
