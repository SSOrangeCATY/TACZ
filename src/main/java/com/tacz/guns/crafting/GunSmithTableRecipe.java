package com.tacz.guns.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.crafting.result.GunSmithTableResult;
import com.tacz.guns.init.ModRecipe;
import com.tacz.guns.resource.pojo.data.recipe.TableRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record GunSmithTableRecipe(
        ResourceLocation id,
        GunSmithTableResult result,
        List<GunSmithTableIngredient> inputs
) implements Recipe<InventoryRecipeInput>{
    public static final Codec<GunSmithTableRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(GunSmithTableRecipe::id),
                    GunSmithTableResult.CODEC.fieldOf("result").forGetter(GunSmithTableRecipe::result),
                    GunSmithTableIngredient.CODEC.listOf().fieldOf("inputs").forGetter(GunSmithTableRecipe::inputs)
            ).apply(instance, GunSmithTableRecipe::new)
    );


    public static final StreamCodec<RegistryFriendlyByteBuf,GunSmithTableRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            GunSmithTableRecipe::id,
            GunSmithTableResult.STREAM_CODEC,
            GunSmithTableRecipe::result,
            ByteBufCodecs.collection(ArrayList::new,GunSmithTableIngredient.STREAM_CODEC),
            GunSmithTableRecipe::inputs,
            GunSmithTableRecipe::new
    );


    public GunSmithTableRecipe(ResourceLocation id, GunSmithTableResult result, List<GunSmithTableIngredient> inputs) {
        this.id = id;
        this.result = result;
        this.inputs = inputs;
    }

    public GunSmithTableRecipe(ResourceLocation id, TableRecipe tableRecipe) {
        this(id, tableRecipe.getResult(), tableRecipe.getMaterials());
    }

    @Override
    @Deprecated
    public boolean matches(InventoryRecipeInput input, Level level) {
        return false;
    }

    @Override
    @Deprecated
    public ItemStack assemble(InventoryRecipeInput input, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.getResult().copy();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.GUN_SMITH_TABLE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipe.GUN_SMITH_TABLE_CRAFTING.get();
    }

    public ItemStack getOutput() {
        return result.getResult();
    }

    public List<GunSmithTableIngredient> getInputs() {
        return inputs;
    }

    public GunSmithTableResult getResult() {
        return result;
    }

    public void init() {
        result.init();
    }

    public ResourceLocation getTab() {
        return result.getGroup();
    }
}
