package com.tacz.guns.api.event.common;

import com.tacz.guns.resource.modifier.AccessoryCacheProperty;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

/**
 * 缓存配件属性修改值时触发的事件
 * <p>
 * 如果有其他模组想要添加自定义的配件属性修改值，可以捕获此事件
 */
public class AttachmentPropertyEvent extends Event implements KubeJSGunEventPoster<AttachmentPropertyEvent> {
    private final ItemStack gunItem;
    private final AccessoryCacheProperty cacheProperty;

    public AttachmentPropertyEvent(ItemStack gunItem, AccessoryCacheProperty attachmentProperty) {
        this.gunItem = gunItem;
        this.cacheProperty = attachmentProperty;
    }

    public ItemStack getGunItem() {
        return gunItem;
    }

    public AccessoryCacheProperty getCacheProperty() {
        return cacheProperty;
    }
}
