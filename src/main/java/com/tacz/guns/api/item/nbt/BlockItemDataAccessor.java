package com.tacz.guns.api.item.nbt;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.item.IBlock;
import com.tacz.guns.api.item.component.BlockComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface BlockItemDataAccessor extends IBlock {
    String BLOCK_ID = "BlockId";

    @Override
    @Nonnull
    default ResourceLocation getBlockId(ItemStack block) {
        return block.getOrDefault(BlockComponents.BLOCK_ID, DefaultAssets.EMPTY_BLOCK_ID);
    }

    @Override
    default void setBlockId(ItemStack block, @Nullable ResourceLocation blockId) {
        if (blockId != null) {
            block.set(BlockComponents.BLOCK_ID, blockId);
        } else {
            block.set(BlockComponents.BLOCK_ID, DefaultAssets.EMPTY_BLOCK_ID);
        }
    }
}