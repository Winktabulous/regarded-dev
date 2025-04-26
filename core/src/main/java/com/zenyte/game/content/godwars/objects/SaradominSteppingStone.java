package com.zenyte.game.content.godwars.objects;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 10/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SaradominSteppingStone implements Shortcut {
    @Override
    public int getLevel(WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {35018};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 8;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        final boolean hasWesternStone = World.getObjectWithType(object.transform(Direction.WEST, 2), 10) != null;
        final boolean hasEasternStone = World.getObjectWithType(object.transform(Direction.EAST, 2), 10) != null;
        if (hasEasternStone && !hasWesternStone) {
            return object.transform(Direction.WEST, 2);
        }
        if (!hasEasternStone && hasWesternStone) {
            return object.transform(Direction.EAST, 2);
        }
        return object;
    }

    @Override
    public int distance(@NotNull final WorldObject object) {
        return isWesternStone(object) ? 1 : 0;
    }

    private final boolean isWesternStone(@NotNull final Location object) {
        final boolean hasWesternStone = World.getObjectWithType(object.transform(Direction.WEST, 2), 10) != null;
        final boolean hasEasternStone = World.getObjectWithType(object.transform(Direction.EAST, 2), 10) != null;
        return !hasWesternStone && hasEasternStone;
    }

    private final boolean isEasternStone(@NotNull final Location object) {
        final boolean hasWesternStone = World.getObjectWithType(object.transform(Direction.WEST, 2), 10) != null;
        final boolean hasEasternStone = World.getObjectWithType(object.transform(Direction.EAST, 2), 10) != null;
        return hasWesternStone && !hasEasternStone;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setMaximumTolerance(true);
        final boolean western = isWesternStone(object);
        if (western) {
            final Location tile = object.transform(Direction.WEST, 2);
            player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
            CharacterLoop.forEach(player.getLocation(), 12, NPC.class, npc -> {
                if (npc.getCombat().getTarget() == player) {
                    npc.getCombat().removeTarget();
                }
            });
        }
        player.faceObject(object);
        WorldTasksManager.scheduleOrExecute(() -> {
            player.faceObject(object);
            final Direction direction = object.getX() > player.getX() ? Direction.EAST : Direction.WEST;
            for (int i = 0; i < 4; i++) {
                WorldTasksManager.schedule(() -> jump(player, direction), (i * 2));
            }
        }, western ? 0 : -1);
    }

    @Override
    public void endSuccess(final Player player, final WorldObject object) {
        player.blockIncomingHits();
        player.setMaximumTolerance(false);
        if (!isWesternStone(object)) {
            player.addWalkSteps(player.getX() - 1, player.getY(), 1, true);
        }
    }

    private void jump(@NotNull final Player player, @NotNull final Direction direction) {
        player.setAnimation(Animation.JUMP);
        player.autoForceMovement(player.getLocation().transform(direction, 2), 30);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
