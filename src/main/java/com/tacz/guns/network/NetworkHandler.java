package com.tacz.guns.network;

import com.tacz.guns.GunMod;
import com.tacz.guns.network.message.*;
import com.tacz.guns.network.message.event.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = GunMod.MOD_ID)
public class NetworkHandler {
    private static final String VERSION = "1.0.4";
    
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(VERSION)
                .executesOn(HandlerThread.MAIN);

        registrar.playToServer(
                ClientMessageCraft.TYPE,
                ClientMessageCraft.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessageCraft::handle,
                        ClientMessageCraft::handle
                )
        );

        registrar.playToServer(
                ClientMessageLaserColor.TYPE,
                ClientMessageLaserColor.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessageLaserColor::handle,
                        ClientMessageLaserColor::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerAim.TYPE,
                ClientMessagePlayerAim.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerAim::handle,
                        ClientMessagePlayerAim::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerBoltGun.TYPE,
                ClientMessagePlayerBoltGun.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerBoltGun::handle,
                        ClientMessagePlayerBoltGun::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerCancelReload.TYPE,
                ClientMessagePlayerCancelReload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerCancelReload::handle,
                        ClientMessagePlayerCancelReload::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerCrawl.TYPE,
                ClientMessagePlayerCrawl.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerCrawl::handle,
                        ClientMessagePlayerCrawl::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerDrawGun.TYPE,
                ClientMessagePlayerDrawGun.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerDrawGun::handle,
                        ClientMessagePlayerDrawGun::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerFireSelect.TYPE,
                ClientMessagePlayerFireSelect.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerFireSelect::handle,
                        ClientMessagePlayerFireSelect::handle
                )
        );

        registrar.playToServer(
                ClientMessagePlayerMelee.TYPE,
                ClientMessagePlayerMelee.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerMelee::handle,
                        ClientMessagePlayerMelee::handle
                )
        );
        registrar.playToServer(
                ClientMessagePlayerReloadGun.TYPE,
                ClientMessagePlayerReloadGun.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerReloadGun::handle,
                        ClientMessagePlayerReloadGun::handle
                )
        );
        registrar.playToServer(
                ClientMessagePlayerShoot.TYPE,
                ClientMessagePlayerShoot.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerShoot::handle,
                        ClientMessagePlayerShoot::handle
                )
        );
        registrar.playToServer(
                ClientMessagePlayerZoom.TYPE,
                ClientMessagePlayerZoom.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessagePlayerZoom::handle,
                        ClientMessagePlayerZoom::handle
                )
        );
        registrar.playToServer(
                ClientMessageRefitGun.TYPE,
                ClientMessageRefitGun.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessageRefitGun::handle,
                        ClientMessageRefitGun::handle
                )
        );
        registrar.playToServer(
                ClientMessageSyncBaseTimestamp.TYPE,
                ClientMessageSyncBaseTimestamp.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessageSyncBaseTimestamp::handle,
                        ClientMessageSyncBaseTimestamp::handle
                )
        );
        registrar.playToServer(
                ClientMessageUnloadAttachment.TYPE,
                ClientMessageUnloadAttachment.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientMessageUnloadAttachment::handle,
                        ClientMessageUnloadAttachment::handle
                )
        );
        registrar.playToClient(
                ServerMessageCraft.TYPE,
                ServerMessageCraft.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageCraft::handle,
                        ServerMessageCraft::handle
                )
        );
        registrar.playToClient(
                ServerMessageLevelUp.TYPE,
                ServerMessageLevelUp.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageLevelUp::handle,
                        ServerMessageLevelUp::handle
                )
        );
        registrar.playToClient(
                ServerMessageRefreshRefitScreen.TYPE,
                ServerMessageRefreshRefitScreen.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageRefreshRefitScreen::handle,
                        ServerMessageRefreshRefitScreen::handle
                )
        );
        registrar.playToClient(
                ServerMessageSound.TYPE,
                ServerMessageSound.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageSound::handle,
                        ServerMessageSound::handle
                )
        );
        registrar.playToClient(
                ServerMessageSwapItem.TYPE,
                ServerMessageSwapItem.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageSwapItem::handle,
                        ServerMessageSwapItem::handle
                )
        );
        registrar.playToClient(
                ServerMessageSyncBaseTimestamp.TYPE,
                ServerMessageSyncBaseTimestamp.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageSyncBaseTimestamp::handle,
                        ServerMessageSyncBaseTimestamp::handle
                )
        );
        registrar.playToClient(
                ServerMessageSyncGunPack.TYPE,
                ServerMessageSyncGunPack.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageSyncGunPack::handle,
                        ServerMessageSyncGunPack::handle
                )
        );
        registrar.playToClient(
                ServerMessageUpdateEntityData.TYPE,
                ServerMessageUpdateEntityData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageUpdateEntityData::handle,
                        ServerMessageUpdateEntityData::handle
                )
        );

        // event
        registrar.playToClient(
                ServerMessageGunDraw.TYPE,
                ServerMessageGunDraw.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunDraw::handle,
                        ServerMessageGunDraw::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunFire.TYPE,
                ServerMessageGunFire.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunFire::handle,
                        ServerMessageGunFire::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunFireSelect.TYPE,
                ServerMessageGunFireSelect.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunFireSelect::handle,
                        ServerMessageGunFireSelect::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunHurt.TYPE,
                ServerMessageGunHurt.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunHurt::handle,
                        ServerMessageGunHurt::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunKill.TYPE,
                ServerMessageGunKill.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunKill::handle,
                        ServerMessageGunKill::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunMelee.TYPE,
                ServerMessageGunMelee.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunMelee::handle,
                        ServerMessageGunMelee::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunReload.TYPE,
                ServerMessageGunReload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunReload::handle,
                        ServerMessageGunReload::handle
                )
        );
        registrar.playToClient(
                ServerMessageGunShoot.TYPE,
                ServerMessageGunShoot.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerMessageGunShoot::handle,
                        ServerMessageGunShoot::handle
                )
        );


    }

    public static void init() {
    }

    public static void sendToClientPlayer(CustomPacketPayload message, Player player) {
        PacketDistributor.sendToPlayer((ServerPlayer) player, message);
    }

    /**
     * 发送给所有监听此实体的玩家
     */
    public static void sendToTrackingEntityAndSelf(Entity centerEntity, CustomPacketPayload message) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(centerEntity, message);
    }

    public static void sendToAllPlayers(CustomPacketPayload message, CustomPacketPayload... messages) {
        PacketDistributor.sendToAllPlayers(message,messages);
    }

    public static void sendToTrackingEntity(CustomPacketPayload message, final Entity centerEntity) {
        PacketDistributor.sendToPlayersTrackingEntity(centerEntity, message);
    }

    public static void sendToDimension(CustomPacketPayload message, final Entity centerEntity) {
        if(centerEntity.level().isClientSide()){
            GunMod.LOGGER.warn("fail send to dimension : level is client side -> {}", message.toString());
            return;
        }
        PacketDistributor.sendToPlayersInDimension((ServerLevel) centerEntity.level(), message);
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer(message);
    }
}
