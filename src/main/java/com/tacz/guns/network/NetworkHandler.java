package com.tacz.guns.network;

import com.tacz.guns.GunMod;
import com.tacz.guns.network.message.*;
import com.tacz.guns.network.message.event.*;
import com.tacz.guns.network.message.handshake.Acknowledge;
import com.tacz.guns.network.message.handshake.ServerMessageSyncedEntityDataMapping;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@EventBusSubscriber(modid = GunMod.MOD_ID)
public class NetworkHandler {
    private static final String VERSION = "1.0.4";
    private static final AtomicInteger ID_COUNT = new AtomicInteger(1);


    @SubscribeEvent // on the mod event bus
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        PayloadRegistrar registrar = event.registrar(VERSION)
                .executesOn(HandlerThread.MAIN);

        registrar.commonToServer(
                ClientMessagePlayerShoot.TYPE,
                ClientMessagePlayerShoot.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerShoot::handle, // 客户端处理器
                        ClientMessagePlayerShoot::handle
                )
        );

        registrar.commonToServer(
                ClientMessagePlayerCancelReload.TYPE,
                ClientMessagePlayerCancelReload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerCancelReload::handle, // 客户端处理器
                        ClientMessagePlayerCancelReload::handle
                )
        );

        registrar.commonToServer(
                ClientMessagePlayerReloadGun.TYPE,
                ClientMessagePlayerReloadGun.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerReloadGun::handle, // 客户端处理器
                        ClientMessagePlayerReloadGun::handle
                )
        );
    }

    public static void init() {
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerFireSelect.class, ClientMessagePlayerFireSelect::encode, ClientMessagePlayerFireSelect::decode, ClientMessagePlayerFireSelect::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerAim.class, ClientMessagePlayerAim::encode, ClientMessagePlayerAim::decode, ClientMessagePlayerAim::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerCrawl.class, ClientMessagePlayerCrawl::encode, ClientMessagePlayerCrawl::decode, ClientMessagePlayerCrawl::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerDrawGun.class, ClientMessagePlayerDrawGun::encode, ClientMessagePlayerDrawGun::decode, ClientMessagePlayerDrawGun::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageSound.class, ServerMessageSound::encode, ServerMessageSound::decode, ServerMessageSound::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessageCraft.class, ClientMessageCraft::encode, ClientMessageCraft::decode, ClientMessageCraft::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageCraft.class, ServerMessageCraft::encode, ServerMessageCraft::decode, ServerMessageCraft::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerZoom.class, ClientMessagePlayerZoom::encode, ClientMessagePlayerZoom::decode, ClientMessagePlayerZoom::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessageRefitGun.class, ClientMessageRefitGun::encode, ClientMessageRefitGun::decode, ClientMessageRefitGun::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageRefreshRefitScreen.class, ServerMessageRefreshRefitScreen::encode, ServerMessageRefreshRefitScreen::decode, ServerMessageRefreshRefitScreen::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessageUnloadAttachment.class, ClientMessageUnloadAttachment::encode, ClientMessageUnloadAttachment::decode, ClientMessageUnloadAttachment::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageSwapItem.class, ServerMessageSwapItem::encode, ServerMessageSwapItem::decode, ServerMessageSwapItem::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerBoltGun.class, ClientMessagePlayerBoltGun::encode, ClientMessagePlayerBoltGun::decode, ClientMessagePlayerBoltGun::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageLevelUp.class, ServerMessageLevelUp::encode, ServerMessageLevelUp::decode, ServerMessageLevelUp::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunHurt.class, ServerMessageGunHurt::encode, ServerMessageGunHurt::decode, ServerMessageGunHurt::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunKill.class, ServerMessageGunKill::encode, ServerMessageGunKill::decode, ServerMessageGunKill::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageUpdateEntityData.class, ServerMessageUpdateEntityData::encode, ServerMessageUpdateEntityData::decode, ServerMessageUpdateEntityData::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageSyncGunPack.class, ServerMessageSyncGunPack::encode, ServerMessageSyncGunPack::decode, ServerMessageSyncGunPack::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessagePlayerMelee.class, ClientMessagePlayerMelee::encode, ClientMessagePlayerMelee::decode, ClientMessagePlayerMelee::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunDraw.class, ServerMessageGunDraw::encode, ServerMessageGunDraw::decode, ServerMessageGunDraw::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunFire.class, ServerMessageGunFire::encode, ServerMessageGunFire::decode, ServerMessageGunFire::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunFireSelect.class, ServerMessageGunFireSelect::encode, ServerMessageGunFireSelect::decode, ServerMessageGunFireSelect::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunMelee.class, ServerMessageGunMelee::encode, ServerMessageGunMelee::decode, ServerMessageGunMelee::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunReload.class, ServerMessageGunReload::encode, ServerMessageGunReload::decode, ServerMessageGunReload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageGunShoot.class, ServerMessageGunShoot::encode, ServerMessageGunShoot::decode, ServerMessageGunShoot::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ServerMessageSyncBaseTimestamp.class, ServerMessageSyncBaseTimestamp::encode, ServerMessageSyncBaseTimestamp::decode, ServerMessageSyncBaseTimestamp::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessageSyncBaseTimestamp.class, ClientMessageSyncBaseTimestamp::encode, ClientMessageSyncBaseTimestamp::decode, ClientMessageSyncBaseTimestamp::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        CHANNEL.registerMessage(ID_COUNT.getAndIncrement(), ClientMessageLaserColor.class, ClientMessageLaserColor::encode, ClientMessageLaserColor::decode, ClientMessageLaserColor::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        registerAcknowledge();
        registerHandshakeMessage(ServerMessageSyncedEntityDataMapping.class, null);
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

    /**
     * 发送给所有监听此实体的玩家
     */
    public static void sendToTrackingEntityAndSelf(Entity centerEntity, Object message) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> centerEntity), message);
    }

    public static void sendToAllPlayers(Object message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToTrackingEntity(Object message, final Entity centerEntity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> centerEntity), message);
    }

    public static void sendToDimension(Object message, final Entity centerEntity) {
        ResourceKey<Level> dimension = centerEntity.level().dimension();
        CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
    }
}
