package com.tacz.guns.entity.sync.core;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

import org.jetbrains.annotations.NotNull;

public class DataHolderAttachmentSerializer implements IAttachmentSerializer<ListTag, DataHolder> {
    @Override
    public @NotNull DataHolder read(@NotNull IAttachmentHolder holder, @NotNull ListTag list, HolderLookup.@NotNull Provider provider)
    {
        DataHolder data = new DataHolder();
        data.deserialize(list, provider);
        return data;
    }

    @Override
    public ListTag write(DataHolder holder, HolderLookup.@NotNull Provider provider)
    {
        return holder.serialize(provider);
    }
}
