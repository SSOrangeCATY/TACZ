package com.tacz.guns.api.item.accessory;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public enum AccessoryType {
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

   public static final StreamCodec<ByteBuf, AccessoryType> STREAM_CODEC = new StreamCodec<>() {
        public @NotNull AccessoryType decode(@NotNull ByteBuf buf) {
            return AccessoryType.values()[VarInt.read(buf)];
        }

        public void encode(@NotNull ByteBuf buf, AccessoryType type) {
            VarInt.write(buf, type.ordinal());
        }
    };

    public static final Codec<AccessoryType> CODEC = Codec.STRING.xmap(
            AccessoryType::valueOf,
            AccessoryType::name
    );
}
