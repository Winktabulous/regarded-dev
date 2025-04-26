package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/04/2019 21:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SinclairMansionLog implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }


    @Override
    public int getLevel(final WorldObject object) {
        return 48;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        //Return an artificial 3x1 object so that pathfinding functions properly.
        if (object.getId() == 16541) {
            return new WorldObject(189, 10, 0, new Location(2722, 3593, 0));
        }
        return object;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                16540, 16541, 16542
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getX() == 2722 && player.getY() == 3592) {
            player.addWalkSteps(2722, 3596, -1, false);
        } else {
            player.addWalkSteps(2722, 3592, -1, false);
        }
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
