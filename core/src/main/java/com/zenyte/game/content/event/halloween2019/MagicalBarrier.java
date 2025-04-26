package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 04/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicalBarrier implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.resetWalkSteps();
        player.addWalkSteps(object.getX(), object.getY(), 1, true);
        player.lock(player.hasWalkSteps() ? 2 : 1);
        WorldTasksManager.scheduleOrExecute(() -> {
            final Location destination = object.transform((object.getRotation() & 1) == 0 ? Direction.EAST : Direction.NORTH, player.matches(object) ? 1 : 0);
            player.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        }, player.hasWalkSteps() ? 1 : -1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.BARRIER.getRepackedObject()};
    }
}
