package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.afk.AfkBarrierDialogue;
import com.zenyte.game.content.skills.afk.AfkSkilling;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;


public class AfkEntranceBarrier implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (option.equalsIgnoreCase("Buy-entry")) {
            player.sendMessage("This is no longer necessary and will be removed soon. Please left click to enter.");
            return;
        }
        Analytics.flagInteraction(player, Analytics.InteractionType.AFK_SKILLING);
        player.resetWalkSteps();
        player.addWalkSteps(object.getX(), object.getY(), 1, true);
        player.lock(player.hasWalkSteps() ? 2 : 1);
        WorldTasksManager.scheduleOrExecute(() -> {
            final Location destination = object.transform((object.getRotation() & 1) == 0 ? Direction.NORTH : Direction.SOUTH, player.matches(object) ? 1 : 0);
            player.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        }, player.hasWalkSteps() ? 1 : -1);

    }

    @Override
    public Object[] getObjects() {
        return new Object[] {55071};
    }
}
