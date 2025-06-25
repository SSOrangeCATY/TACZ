package com.tacz.guns.resource.index;

import com.google.common.base.Preconditions;
import com.tacz.guns.api.item.accessory.AccessoryType;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.pojo.AccessoryIndexPOJO;
import com.tacz.guns.resource.pojo.data.accessory.AccessoryData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CommonAttachmentIndex {
    private AccessoryData data;
    private AccessoryType type;
    private AccessoryIndexPOJO pojo;
    private int sort;

    private CommonAttachmentIndex() {
    }

    public static CommonAttachmentIndex getInstance(AccessoryIndexPOJO attachmentIndexPOJO) throws IllegalArgumentException {
        CommonAttachmentIndex index = new CommonAttachmentIndex();
        index.pojo = attachmentIndexPOJO;
        checkIndex(attachmentIndexPOJO, index);
        checkData(attachmentIndexPOJO, index);
        return index;
    }

    private static void checkIndex(AccessoryIndexPOJO attachmentIndexPOJO, CommonAttachmentIndex index) {
        Preconditions.checkArgument(attachmentIndexPOJO != null, "index object file is empty");
        Preconditions.checkArgument(attachmentIndexPOJO.getType() != null, "attachment type must be nonnull.");
        index.type = attachmentIndexPOJO.getType();
        index.sort = Mth.clamp(attachmentIndexPOJO.getSort(), 0, 65536);
    }

    private static void checkData(AccessoryIndexPOJO attachmentIndexPOJO, CommonAttachmentIndex index) {
        ResourceLocation pojoData = attachmentIndexPOJO.getData();
        Preconditions.checkArgument(pojoData != null, "index object missing pojoData field");
        AccessoryData data = CommonAssetsManager.get().getAttachmentData(pojoData);
        Preconditions.checkArgument(data != null, "there is no corresponding data file");
        index.data = data;
    }

    public AccessoryData getData() {
        return data;
    }

    public AccessoryType getType() {
        return type;
    }

    public AccessoryIndexPOJO getPojo() {
        return pojo;
    }

    public int getSort() {
        return sort;
    }
}
