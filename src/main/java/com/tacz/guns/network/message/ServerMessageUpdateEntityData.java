package com.tacz.guns.network.message;

import com.tacz.guns.GunMod;
import com.tacz.guns.entity.sync.core.DataEntry;
import com.tacz.guns.entity.sync.core.SyncedEntityData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record ServerMessageUpdateEntityData(
        int entityId,
        List<DataEntry<?, ?>> entries
) implements CustomPacketPayload {

    public static final Type<ServerMessageUpdateEntityData> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "server_update_entity_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMessageUpdateEntityData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerMessageUpdateEntityData::entityId,
                    ByteBufCodecs.collection(ArrayList::new,DataEntry.STREAM_CODEC),
                    ServerMessageUpdateEntityData::entries,
                    ServerMessageUpdateEntityData::new
            );

    public static void handle(ServerMessageUpdateEntityData message, IPayloadContext context) {
        if (context.flow().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level == null) return;

                Entity entity = level.getEntity(message.entityId());
                if (entity == null) return;

                SyncedEntityData instance = SyncedEntityData.instance();
                message.entries().forEach(entry ->
                        instance.set(entity, entry.getKey(), entry.getValue())
                );
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}