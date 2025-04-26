package com.zenyte.game.content.boss.zulrah.combat;

import com.zenyte.game.content.boss.zulrah.Sequence;
import com.zenyte.game.content.boss.zulrah.ZulrahInstance;
import com.zenyte.game.content.boss.zulrah.ZulrahNPC;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.LocationUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 19. march 2018 : 20:07.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class MeleeSequence implements Sequence {
    private static final Animation LEFT_MELEE_ANIM = new Animation(5806);
    private static final Animation RIGHT_MELEE_ANIM = new Animation(5807);
    private static final Graphics STUN_GFX = new Graphics(254, 0, 96);
    private static final Location EASTERN_SAFESPOT2 = new Location(2273, 3072, 0);
    private static final Location EASTERN_SAFESPOT = new Location(2272, 3072, 0);
    private static final Location WESTERN_SAFESPOT = new Location(2264, 3072, 0);
    private static final Location WESTERN_SAFESPOT2 = new Location(2263, 3072, 0);

    private static final boolean isSafespot(final ZulrahInstance instance, final Location tile) {
        return instance.getLocation(EASTERN_SAFESPOT).getPositionHash() == tile.getPositionHash() || instance.getLocation(WESTERN_SAFESPOT).getPositionHash() == tile.getPositionHash() || instance.getLocation(EASTERN_SAFESPOT2).getPositionHash() == tile.getPositionHash() || instance.getLocation(WESTERN_SAFESPOT2).getPositionHash() == tile.getPositionHash();
    }

    @Override
    public void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player player) {
        zulrah.lock();
        WorldTasksManager.schedule(new WorldTask() {
            private Location tile;
            private int ticks;
            @Override
            public void run() {
                if (zulrah.isCancelled(true)) {
                    zulrah.lock(3);
                    stop();
                    return;
                }
                if (zulrah.isDead()) {
                    stop();
                    return;
                }
                if (zulrah.isStopped()) {
                    return;
                }
                switch (ticks) {
                case 0: 
                case 9: 
                    zulrah.setFaceEntity(null);
                    tile = new Location(player.getLocation());
                    final Location zul = new Location(zulrah.getLocation());
                    if (instance.getLocation(2272, 3078, 0).getY() == tile.getY()) {
                        if (tile.getX() < (zul.getX() - 1)) {
                            zulrah.setFaceLocation(tile);
                            zulrah.setAnimation(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? RIGHT_MELEE_ANIM : LEFT_MELEE_ANIM);
                        } else if (tile.getX() > (zul.getX() + 5)) {
                            zulrah.setFaceLocation(tile);
                            zulrah.setAnimation(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? LEFT_MELEE_ANIM : RIGHT_MELEE_ANIM);
                        }
                    } else {
                        if (tile.getX() < (zul.getX() - 1)) {
                            zulrah.setFaceLocation(instance.getLocation(2263, 3074, 0));
                            zulrah.setAnimation(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? RIGHT_MELEE_ANIM : LEFT_MELEE_ANIM);
                        } else if (tile.getX() > (zul.getX() + 5)) {
                            zulrah.setFaceLocation(instance.getLocation(2273, 3074, 0));
                            zulrah.setAnimation(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? LEFT_MELEE_ANIM : RIGHT_MELEE_ANIM);
                        } else {
                            zulrah.setFaceLocation(instance.getLocation(2268, 3069, 0));
                            zulrah.setAnimation(tile.getX() >= instance.getLocation(2268, 0, 0).getX() ? LEFT_MELEE_ANIM : RIGHT_MELEE_ANIM);
                        }
                    }
                    break;
                case 5: 
                case 15: 
                    final boolean isSafe = ticks == 5 && zulrah.getRotation() <= 1 && player.getLocation().matches(instance.getLocation(2274, 3077, 0));
                    if (!ProjectileUtils.isProjectileClipped(null, null, tile, zulrah, false) && !isSafe && player.getLocation().withinDistance(tile, 1) && !isSafespot(instance, player.getLocation())) {
                        final Location middle = zulrah.getMiddleLocation().transform(0, 1, 0);
                        double degrees = Math.toDegrees(Math.atan2(player.getY() - middle.getY(), player.getX() - middle.getX()));
                        if (degrees < 0) {
                            degrees += 360;
                        }
                        final double angle = Math.toRadians(degrees);
                        final int px = (int) Math.round(middle.getX() + (zulrah.getSize() + 4) * Math.cos(angle));
                        final int py = (int) Math.round(middle.getY() + (zulrah.getSize() + 4) * Math.sin(angle));
                        final List<Location> tiles = LocationUtil.calculateLine(player.getX(), player.getY(), px, py, player.getPlane());
                        if (!tiles.isEmpty()) tiles.remove(0);
                        final Location destination = new Location(player.getLocation());
                        for (final Location tile : tiles) {
                            final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
                            if (dir == -1) {
                                continue;
                            }
                            if (!World.checkWalkStep(destination.getPlane(), destination.getX(), destination.getY(), dir, player.getSize(), false, false)) break;
                            destination.setLocation(tile);
                        }
                        final int direction = DirectionUtil.getFaceDirection(player.getX() - destination.getX(), player.getY() - destination.getY());
                        if (!destination.matches(player)) {
                            player.setForceMovement(new ForceMovement(destination, 30, direction));
                            player.lock();
                        }
                        player.faceEntity(zulrah);
                        player.setAnimation(Animation.KNOCKBACK);
                        WorldTasksManager.schedule(() -> {
                            player.setLocation(destination);
                            player.unlock();
                        });
                        player.stun(5);
                        player.sendMessage("You have been stunned!");
                        player.cancelCombat();
                        player.setGraphics(STUN_GFX);
                        player.applyHit(new Hit(zulrah, Utils.random(41), HitType.DEFAULT));
                        player.getToxins().applyToxin(ToxinType.VENOM, 6, zulrah);
                    }
                    if (ticks == 15) {
                        zulrah.lock(3);
                        stop();
                    }
                    break;
                }
                ticks++;
            }
        }, 0, 0);
    }
}
