package com.tacz.guns.api.item.attachment;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public enum AttachmentType {
    /**
     * 瞄具
     */
    @SerializedName("scope")
    SCOPE,
    /**
     * 枪口组件
     */
    @SerializedName("muzzle")
    MUZZLE,
    /**
     * 枪托
     */
    @SerializedName("stock")
    STOCK,
    /**
     * 握把
     */
    @SerializedName("grip")
    GRIP,
    /**
     * 激光指示器
     */
    @SerializedName("laser")
    LASER,
    /**
     * 扩容弹夹（匣）
     */
    @SerializedName("extended_mag")
    EXTENDED_MAG,
    /**
     * 用来表示物品不是配件的情况。
     */
    NONE;

   public static final StreamCodec<ByteBuf, AttachmentType> STREAM_CODEC = new StreamCodec<>() {
        public @NotNull AttachmentType decode(@NotNull ByteBuf buf) {
            return AttachmentType.values()[VarInt.read(buf)];
        }

        public void encode(@NotNull ByteBuf buf, AttachmentType type) {
            VarInt.write(buf, type.ordinal());
        }
    };

    public static final Codec<AttachmentType> CODEC = Codec.STRING.xmap(
            AttachmentType::valueOf,
            AttachmentType::name
    );
}
