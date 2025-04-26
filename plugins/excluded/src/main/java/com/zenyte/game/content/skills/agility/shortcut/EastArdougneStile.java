package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 17:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EastArdougneStile implements Shortcut {
    private static final Animation CLIMB = new Animation(839);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (object.matches(new Location(2647, 3375, 0))) {
            return player.getX() < object.getX() ? new Location(2646, 3375, 0) : new Location(2649, 3375, 0);
        } else if (object.matches(new Location(2638, 3350, 0))) {
            return player.getX() < object.getX() ? new Location(2637, 3350, 0) : new Location(2640, 3350, 0);
        } else if (object.matches(new Location(2917, 3438, 0))) {
            return player.getX() < object.getX() ? new Location(2916, 3438, 0) : new Location(2919, 3438, 0);
        }
        return player.getX() < object.getX() ? new Location(2654, 3347, 0) : new Location(2657, 3347, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final Location destination = object.transform(player.getX() > object.getX() ? -1 : 2, 0, 0);
        final ForceMovement forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb over the stile." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {993};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
