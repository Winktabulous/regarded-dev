package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 18:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrollStrongholdSmallRocksClimb implements Shortcut {

    private static final Location LV_41_TOP = new Location(2872, 3671, 0);
    private static final Location LV_41_BOTTOM = new Location(2869, 3671, 0);
    private static final Location LV_41_TOP_OBJ = new Location(2871, 3671, 0);
    private static final Location LV_41_BOTTOM_OBJ = new Location(2870, 3671, 0);

    private static final Location LV_43_TOP = new Location(2878, 3668, 0);
    private static final Location LV_43_BOTTOM = new Location(2878, 3665, 0);
    private static final Location LV_43_TOP_OBJ = new Location(2878, 3667, 0);
    private static final Location LV_43_BOTTOM_OBJ = new Location(2878, 3666, 0);

    private static final Location LV_47_TOP = new Location(2900, 3680, 0);
    private static final Location LV_47_BOTTOM = new Location(2903, 3680, 0);
    private static final Location LV_47_TOP_OBJ = new Location(2901, 3680, 0);
    private static final Location LV_47_BOTTOM_OBJ = new Location(2902, 3680, 0);

    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getLevel(final WorldObject object) {
        if (object.matches(LV_41_TOP_OBJ) || object.matches(LV_41_BOTTOM_OBJ)) {
            return 41;
        } else if (object.matches(LV_43_TOP_OBJ) || object.matches(LV_43_BOTTOM_OBJ)) {
            return 43;
        } else if (object.matches(LV_47_TOP_OBJ) || object.matches(LV_47_BOTTOM_OBJ)) {
            return 47;
        }
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                16521, 16522, 16524
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        Location destination;
        ForceMovement forceMovement;
        if (object.matches(LV_41_BOTTOM_OBJ) || object.matches(LV_41_TOP_OBJ)) {
            destination = object.matches(LV_41_TOP_OBJ) ? LV_41_BOTTOM : LV_41_TOP;
            forceMovement = new ForceMovement(destination, 60, ForceMovement.EAST);
        } else if (object.matches(LV_43_TOP_OBJ) || object.matches(LV_43_BOTTOM_OBJ)) {
            destination = object.matches(LV_43_TOP_OBJ) ? LV_43_BOTTOM : LV_43_TOP;
            forceMovement = new ForceMovement(destination, 60, ForceMovement.NORTH);
        } else if (object.matches(LV_47_TOP_OBJ) || object.matches(LV_47_BOTTOM_OBJ)) {
            destination = object.matches(LV_47_TOP_OBJ) ? LV_47_BOTTOM : LV_47_TOP;
            forceMovement = new ForceMovement(destination, 60, ForceMovement.WEST);
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
    public double getSuccessXp(final WorldObject object) {
        return getLevel(object) == 47 ? 8 : 0;
    }
}
