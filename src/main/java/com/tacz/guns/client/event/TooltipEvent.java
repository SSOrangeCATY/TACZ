package com.tacz.guns.client.event;

import com.tacz.guns.api.item.nbt.AmmoItemDataAccessor;
import com.tacz.guns.api.item.nbt.AccessoryItemDataAccessor;
import com.tacz.guns.api.item.nbt.BlockItemDataAccessor;
import com.tacz.guns.api.item.nbt.GunItemDataAccessor;
import com.tacz.guns.config.client.RenderConfig;
import com.tacz.guns.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


@EventBusSubscriber
public class TooltipEvent {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (event.getFlags().isAdvanced() && RenderConfig.ENABLE_TACZ_ID_IN_TOOLTIP.get()) {
            switch (event.getItemStack().getItem()) {
                case GunItemDataAccessor item ->
                        event.getToolTip().add(formatTooltip(GunItemDataAccessor.GUN_ID_TAG, item.getGunId(event.getItemStack())));
                case AmmoItemDataAccessor item ->
                        event.getToolTip().add(formatTooltip(AmmoItemDataAccessor.AMMO_ID_TAG, item.getAmmoId(event.getItemStack())));
                case AccessoryItemDataAccessor item ->
                        event.getToolTip().add(formatTooltip(AccessoryItemDataAccessor.ATTACHMENT_ID_TAG, item.getAccessoryId(event.getItemStack())));
                case BlockItemDataAccessor item when !ModItems.GUN_SMITH_TABLE.get().equals(item) ->
                        event.getToolTip().add(formatTooltip(BlockItemDataAccessor.BLOCK_ID, item.getBlockId(event.getItemStack())));
                default -> {
                }
            }
        }
    }

    public static Component formatTooltip(String key, ResourceLocation value) {
        return Component.literal(String.format("%s: \"%s\"", key, value)).withStyle(ChatFormatting.DARK_GRAY);
    }
}
