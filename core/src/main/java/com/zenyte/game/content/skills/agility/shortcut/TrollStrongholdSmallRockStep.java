package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 18:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrollStrongholdSmallRockStep implements Shortcut {

    private static final Animation CLIMB = new Animation(839);

    private static final Location TOP_WEIRD_ROCK = new Location(2908, 3682, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        Location destination;
        ForceMovement forceMovement;
        if (object.getId() == 3803 || object.getId() == 3804 || object.getId() == 3748) {
            if (object.withinDistance(2888, 3660, 3) || object.withinDistance(2856, 3611, 3) || object.withinDistance(2834, 3628, 3)) {
                destination = object.transform(0, player.getY() > object.getY() ? -1 : 1, 0);
                forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
            } else {
                destination = object.transform(player.getX() > object.getX() ? -1 : 1, 0, 0);
                forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
            }
        } else if (object.getId() == ObjectId.ROCKS_16523) {
            if (object.matches(TOP_WEIRD_ROCK)) {
                destination = object.transform(player.getX() > object.getX() ? -1 : 1, 0, 0);
                forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
            } else {
                destination = object.transform(0, player.getY() > object.getY() ? -1 : 1, 0);
                forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
            }
        } else if (object.getId() >= 26400 && object.getId() <= 26402) {
            destination = object.getY() >= 3760 ? new Location(2928, 3757, 0) : object.transform(0, player.getY() > object.getY() ? -2 : 2, 0);
            forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
        } else {
            throw new IllegalStateException("Unknown obstacle");
        }
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
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return (object.getId() >= 26400 && object.getId() <= 26402) && object.getY() >= 3760 ? new Location(2928, 3760, 0) : object;
    }

    @Override
    public int getLevel(final WorldObject object) {
        if (object.withinDistance(2856, 3611, 3) || object.withinDistance(2834, 3628, 3) || object.withinDistance(2821, 3635, 3)) {
            return 0;
        }
        return (object.getId() == 3803 || object.getId() == 3804) ? 43 : object.getId() == 3748 ? 44 : object.getId() == 16523 ? 44 : (object.getId() >= 26400 && object.getId() <= 26402) ? 60 : 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 3803, 3804, 3748, 16523, 26400, 26401, 26402 };
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
