package com.tacz.guns.resource.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum DataType {
    /**
     * 需要同步到客户端的数据类型
     */
    GUN_DATA,
    ATTACHMENT_DATA,
    AMMO_INDEX,
    GUN_INDEX,
    ATTACHMENT_INDEX,
    RECIPES,
    RECIPE_FILTER,
    ATTACHMENT_TAGS,
    ALLOW_ATTACHMENT_TAGS,
    BLOCK_DATA,
    BLOCK_INDEX;

    public static StreamCodec<ByteBuf,DataType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DataType::ordinal,
            (i)-> DataType.values()[i]
    );
}
