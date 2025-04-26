package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OgrePenLooseRailing implements Shortcut {
    private static final Animation animation = new Animation(1237);
    private static final Location eastPosition = new Location(2523, 3375, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (player.getX() <= object.getX()) {
            player.addWalkSteps(object.getX(), object.getY(), 1, true);
            WorldTasksManager.schedule(() -> subHandle(player, object));
            return;
        }
        subHandle(player, object);
    }

    private void subHandle(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean west = player.matches(object);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(animation);
                    player.setForceMovement(new ForceMovement(west ? eastPosition : object, 60, west ? ForceMovement.WEST : ForceMovement.EAST));
                } else if (ticks == 2) {
                    player.setLocation(west ? eastPosition : object);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {19171};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
