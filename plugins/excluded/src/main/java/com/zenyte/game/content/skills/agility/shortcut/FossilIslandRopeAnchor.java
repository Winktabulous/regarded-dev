package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 21:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FossilIslandRopeAnchor implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }


    @Override
    public int getLevel(final WorldObject object) {
        return 64;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.getX() == 3779 ? new Location(3778, 3821, 0) : new Location(3784, 3821, 0);
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                30916, 30917
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getX() == 3778 && player.getY() == 3821) {
            player.addWalkSteps(3784, 3821, -1, false);
        } else {
            player.addWalkSteps(3778, 3821, -1, false);
        }
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
