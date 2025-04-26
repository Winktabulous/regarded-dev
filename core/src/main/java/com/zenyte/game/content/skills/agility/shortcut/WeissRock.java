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
 * @author Kris | 10/05/2019 18:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WeissRock implements Shortcut {
    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                33312
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    private static final Animation CLIMB = new Animation(839);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        Location destination;
        ForceMovement forceMovement;
        destination = player.getX() >= 2852 ? new Location(2850, 3936, 0) : new Location(2852, 3936, 0);
        forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb over the rocks." : null;
    }


    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
