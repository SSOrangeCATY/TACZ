package com.tacz.guns.util;

import com.tacz.guns.config.common.AmmoConfig;
import com.tacz.guns.util.block.ProjectileExplosion;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public final class ExplodeUtil {
    private ExplodeUtil() {} // 工具类私有构造

    public static void createExplosion(Entity owner, Entity exploder, float damage, float radius,
                                       boolean knockback, boolean destroy, Vec3 hitPos) {
        if (!(exploder.level() instanceof ServerLevel level)) {
            return; // 客户端不执行
        }

        Explosion.BlockInteraction mode = getExplosionMode(destroy);
        ProjectileExplosion explosion = createProjectileExplosion(level, owner, exploder, hitPos, damage, radius, knockback, mode);

        if (EventHooks.onExplosionStart(level, explosion)) {
            return; // 事件取消则不执行
        }

        executeExplosion(explosion, mode);
        sendExplosionPackets(level, hitPos, radius, explosion);
    }

    private static Explosion.BlockInteraction getExplosionMode(boolean destroy) {
        return destroy ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
    }

    private static ProjectileExplosion createProjectileExplosion(ServerLevel level, Entity owner, Entity exploder,
                                                                 Vec3 hitPos, float damage, float radius,
                                                                 boolean knockback, Explosion.BlockInteraction mode) {
        return new ProjectileExplosion(
                level, owner, exploder, null, null,
                hitPos.x(), hitPos.y(), hitPos.z(),
                damage, radius, knockback, mode
        );
    }

    private static void executeExplosion(ProjectileExplosion explosion, Explosion.BlockInteraction mode) {
        explosion.explode();
        explosion.finalizeExplosion(true);
        if (mode == Explosion.BlockInteraction.KEEP) {
            explosion.clearToBlow();
        }
    }

    private static void sendExplosionPackets(ServerLevel level, Vec3 hitPos, float radius, ProjectileExplosion explosion) {
        double visibleDistance = AmmoConfig.EXPLOSIVE_AMMO_VISIBLE_DISTANCE.get();
        float squaredDistance = (float) (visibleDistance * visibleDistance);

        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(hitPos) < squaredDistance) {
                sendExplosionPacket(player, hitPos, radius, explosion);
            }
        }
    }

    private static void sendExplosionPacket(ServerPlayer player, Vec3 hitPos, float radius, ProjectileExplosion explosion) {
        Vec3 knockback = explosion.getHitPlayers().get(player);
        ClientboundExplodePacket packet = new ClientboundExplodePacket(
                hitPos.x(), hitPos.y(), hitPos.z(),
                radius,
                explosion.getToBlow(),
                knockback,
                explosion.getBlockInteraction(),
                explosion.getSmallExplosionParticles(),
                explosion.getLargeExplosionParticles(),
                explosion.getExplosionSound()
        );
        player.connection.send(packet);
    }
}