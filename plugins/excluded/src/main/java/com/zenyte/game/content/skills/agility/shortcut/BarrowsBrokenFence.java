package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 04/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsBrokenFence implements Shortcut {
    private static final Animation climbAnimation = new Animation(839);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getY() < object.getY() ? object.transform(Direction.SOUTH) : object.transform(Direction.NORTH, 2);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.autoForceMovement(object.transform(0, player.getY() < object.getY() ? 2 : -1, 0), 60);
        player.setAnimation(climbAnimation);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb over the broken fence." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 18411 };
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
