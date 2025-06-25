package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.client.event.SwapItemWithOffHand;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerMessageSwapItem implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerMessageSwapItem> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_swap_item"));

    public static final StreamCodec<ByteBuf, ServerMessageSwapItem> STREAM_CODEC =
            StreamCodec.unit(new ServerMessageSwapItem());

    public static void handle(ServerMessageSwapItem message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                NeoForge.EVENT_BUS.post(new SwapItemWithOffHand());
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ServerMessageSwapItem;
    }
}