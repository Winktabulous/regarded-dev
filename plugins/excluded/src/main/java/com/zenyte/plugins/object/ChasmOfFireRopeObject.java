package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24-3-2019 | 22:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChasmOfFireRopeObject implements ObjectAction {

    public static final Animation CLIMB_UP = new Animation(828);

    public static final Animation CLIMB_DOWN = new Animation(827);

    private static final Location INSIDE_LOCATION = new Location(1434, 10078, 3);

    private static final Location OUTSIDE_LOCATION = new Location(1435, 3671, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean down = object.getId() == 30236;
        player.lock(1);
        player.setAnimation(down ? CLIMB_DOWN : CLIMB_UP);
        WorldTasksManager.schedule(() -> player.setLocation(down ? INSIDE_LOCATION : OUTSIDE_LOCATION));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHASM_30236, ObjectId.ROPE_30234 };
    }
}
