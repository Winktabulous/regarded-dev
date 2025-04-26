package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 17/11/2019 | 19:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ZeahMysticalBarrier implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final int rotation = object.getRotation();
        player.setRunSilent(2);
        if (rotation == 0) {
            player.addWalkSteps(player.getX(), object.getY() + (player.getY() < object.getY() ? 1 : -1), -1, false);
        } else {
            player.addWalkSteps(object.getX() + (player.getX() < object.getX() ? 1 : -1), player.getY(), -1, false);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34642, 34643, 34644, 34645, 34646 };
    }
}
