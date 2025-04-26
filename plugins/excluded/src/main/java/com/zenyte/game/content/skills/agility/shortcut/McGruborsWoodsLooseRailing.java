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
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class McGruborsWoodsLooseRailing implements Shortcut {
    private static final Animation squeezeAnimation = new Animation(1237);
    private static final Location east = new Location(2662, 3500, 0);
    private static final Location west = new Location(2661, 3500, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (player.getX() >= 2662 && !player.getLocation().matches(east)) {
            player.addWalkSteps(east.getX(), east.getY(), 1, true);
            WorldTasksManager.schedule(() -> subHandle(player, object));
            return;
        }
        subHandle(player, object);
    }

    private void subHandle(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean east = player.getLocation().getPositionHash() == object.getPositionHash();
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(squeezeAnimation);
                    player.setForceMovement(new ForceMovement(east ? west : McGruborsWoodsLooseRailing.east, 60, east ? ForceMovement.WEST : ForceMovement.EAST));
                } else if (ticks == 2) {
                    player.setLocation(east ? west : McGruborsWoodsLooseRailing.east);
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
        return new int[] {51};
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
