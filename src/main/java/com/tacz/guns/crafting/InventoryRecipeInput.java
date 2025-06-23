package com.tacz.guns.crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record InventoryRecipeInput(Inventory inventory) implements RecipeInput {
    public ItemStack getItem(int slot){
        return inventory.getItem(slot);
    };

    public int size(){
        return inventory.getContainerSize();
    };

    public boolean isEmpty() {
        return inventory.isEmpty();
    }
}