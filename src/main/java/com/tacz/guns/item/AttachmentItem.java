package com.tacz.guns.item;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAccessory;
import com.tacz.guns.api.item.accessory.AccessoryType;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.component.GunComponents;
import com.tacz.guns.api.item.nbt.AccessoryItemDataAccessor;
import com.tacz.guns.client.renderer.item.AttachmentItemRenderer;
import com.tacz.guns.client.resource.index.ClientAccessoryIndex;
import com.tacz.guns.inventory.tooltip.AttachmentItemTooltip;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


public class AttachmentItem extends Item implements AccessoryItemDataAccessor {
    public AttachmentItem() {
        super(new Properties().stacksTo(1).component(DataComponents.CUSTOM_DATA, CustomData.EMPTY));
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public Component getName(@Nonnull ItemStack stack) {
        ResourceLocation accessoryId = this.getAccessoryId(stack);
        Optional<ClientAccessoryIndex> attachmentIndex = TimelessAPI.getClientAttachmentIndex(accessoryId);
        if (attachmentIndex.isPresent()) {
            return Component.translatable(attachmentIndex.get().getName());
        }
        return super.getName(stack);
    }

    private static Comparator<Map.Entry<ResourceLocation, CommonAttachmentIndex>> idNameSort() {
        return Comparator.comparingInt(m -> m.getValue().getSort());
    }

    public static NonNullList<ItemStack> fillItemCategory(AccessoryType type) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        TimelessAPI.getAllCommonAttachmentIndex().stream().sorted(idNameSort()).forEach(entry -> {
            if (entry.getValue().getPojo().isHidden()) {
                return;
            }
            if (type.equals(entry.getValue().getType())) {
                ItemStack itemStack = AttachmentItemBuilder.create().setId(entry.getKey()).build();
                itemStack.set(DataComponents.ITEM_NAME, Component.translatable(entry.getValue().getPojo().getName()));
                itemStack.set(GunComponents.DISPLAY_ID,entry.getValue().getPojo().getDisplay());
                stacks.add(itemStack);
            }
        });
        return stacks;
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                Minecraft minecraft = Minecraft.getInstance();
                return new AttachmentItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
            }
        });
    }

    @Override
    @Nonnull
    public AccessoryType getType(ItemStack attachmentStack) {
        IAccessory iAttachment = IAccessory.getIAttachmentOrNull(attachmentStack);
        if (iAttachment != null) {
            ResourceLocation id = iAttachment.getAccessoryId(attachmentStack);
            return TimelessAPI.getCommonAttachmentIndex(id).map(CommonAttachmentIndex::getType).orElse(AccessoryType.NONE);
        } else {
            return AccessoryType.NONE;
        }
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return Optional.of(new AttachmentItemTooltip(this.getAccessoryId(stack), this.getType(stack), stack));
    }
}
