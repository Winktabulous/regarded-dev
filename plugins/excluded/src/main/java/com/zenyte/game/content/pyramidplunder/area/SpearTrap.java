package com.zenyte.game.content.pyramidplunder.area;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/2/2020
 */
public enum SpearTrap {
    FIRST_TRAP(1, new Location(1927, 4472, 0), false), SECOND_TRAP(2, new Location(1954, 4473, 0), false), THIRD_TRAP(3, new Location(1976, 4465, 0), false), FOURTH_TRAP(4, new Location(1930, 4452, 0), true), FIFTH_TRAP(5, new Location(1961, 4444, 0), true), SIXTH_TRAP(6, new Location(1926, 4427, 0), false), SEVENTH_TRAP(7, new Location(1944, 4424, 0), false), EIGHTH_TRAP(8, new Location(1974, 4423, 0), false);
    public static final int TRAP_STATUS_VARBIT = 2365;
    public static final int INACTIVE_STAGE = 1;
    public static final SpearTrap[] traps = SpearTrap.values();
    public static final int ACTIVE_STAGE = 0;
    private static final Animation spearAnim = new Animation(459);
    private static final ForceTalk hitTalk = new ForceTalk("Ouch!");
    private final int roomId;
    private final ImmutableLocation southwesternCorner;
    private final boolean horizontal;

    SpearTrap(final int roomId, final Location southwesternCorner, final boolean horizontal) {
        this.roomId = roomId;
        this.southwesternCorner = new ImmutableLocation(southwesternCorner);
        this.horizontal = horizontal;
    }

    public static void reset(@NotNull final Player player) {
        player.getVarManager().sendBit(SpearTrap.TRAP_STATUS_VARBIT, ACTIVE_STAGE);
    }

    public static SpearTrap get(final int roomId) {
        for (SpearTrap trap : traps) {
            if (trap.getRoomId() == roomId) {
                return trap;
            }
        }
        throw new IllegalArgumentException("No pyramid spear trap defined for room: " + roomId);
    }

    public boolean execute(@NotNull final Player player, final int x, final int y) {
        if (within(x, y)) {
            if (player.getVarManager().getBitValue(TRAP_STATUS_VARBIT) == INACTIVE_STAGE) {
                return false;
            }
            final Location currentTile = new Location(player.getLocation());
            final WorldObject trapObject = findTrap(currentTile);
            final int baseDelay = 1;
            final Direction movementDirection = findEmptyMovementTile(currentTile);
            final Location destination = currentTile.transform(movementDirection);
            player.lock(baseDelay + 3);
            WorldTasksManager.schedule(() -> {
                if (!player.inArea(PyramidPlunderArea.class)) {
                    return;
                }
                final WorldObject object = World.getObjectWithType(trapObject, 10);
                World.sendObjectAnimation(object, spearAnim);
                player.setForceTalk(hitTalk);
                player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR).setExecuteIfLocked());
                final ForceMovement forceMovement = new ForceMovement(player.getLocation(), 10, destination, 30, movementDirection.getDirection());
                player.setForceMovement(forceMovement);
            }, baseDelay);
            WorldTasksManager.schedule(() -> {
                if (!player.inArea(PyramidPlunderArea.class)) {
                    return;
                }
                player.setLocation(destination);
                player.unlock();
            }, baseDelay + 1);
            return true;
        }
        return false;
    }

    private final Direction findEmptyMovementTile(@NotNull final Location trapActivationTile) {
        for (final Direction direction : Direction.cardinalDirections) {
            final Location position = trapActivationTile.transform(direction);
            if (within(position.getX(), position.getY())) {
                continue;
            }
            if (ProjectileUtils.isProjectileClipped(null, null, trapActivationTile, position, true, false)) {
                continue;
            }
            return direction;
        }
        throw new IllegalStateException();
    }

    public boolean within(final int x, final int y) {
        final int swx = southwesternCorner.getX();
        final int swy = southwesternCorner.getY();
        return x >= swx && x <= (swx + 1) && y >= swy && y <= (swy + 1);
    }

    private final WorldObject findTrap(@NotNull final Location trapHitTile) {
        //First we check if the trap is underneath the tile, as that is a possibility.
        final WorldObject objectUnderneath = World.getObjectWithType(trapHitTile, 10);
        if (objectUnderneath != null) {
            return objectUnderneath;
        }
        //If the trap wasn't underneath, it has to be on a tile to which the player cannot walk.
        for (final Direction direction : Direction.cardinalDirections) {
            final Location position = trapHitTile.transform(direction);
            final WorldObject object = World.getObjectWithType(position, 10);
            if (object != null && ProjectileUtils.isProjectileClipped(null, null, trapHitTile, position, true, false)) {
                return object;
            }
        }
        throw new IllegalStateException();
    }

    public int getRoomId() {
        return roomId;
    }

    public ImmutableLocation getSouthwesternCorner() {
        return southwesternCorner;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
