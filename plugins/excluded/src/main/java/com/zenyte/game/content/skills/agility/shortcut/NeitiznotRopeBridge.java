package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 17:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NeitiznotRopeBridge implements Shortcut {
    @Override
    public int getLevel(final WorldObject object) {
        return 40;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {21306, 21307, 21310, 21311, 21308, 21309, 21312, 21313, 21314, 21315};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 9;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean north = player.getY() > object.getY();
        player.addWalkSteps(player.getX(), north ? (player.getY() - 9) : (player.getY() + 9), 9, false);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
