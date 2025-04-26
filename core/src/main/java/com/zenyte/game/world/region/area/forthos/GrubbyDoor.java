package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Andys1814
 */
public final class GrubbyDoor implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("You attempt to picklock the door..");
        if (player.getSkills().getLevel(SkillConstants.THIEVING) < 57) {
            player.sendMessage("You fail to open it!");
            return;
        }
        player.sendMessage("The door opens!");
        player.getSkills().addXp(SkillConstants.THIEVING, 10);
        player.addMovementLock(new MovementLock(System.currentTimeMillis() + (player.hasWalkSteps() ? 1000 : 300)).setFullLock());
        WorldTasksManager.scheduleOrExecute(() -> walkThrough(player, object), player.hasWalkSteps() ? 0 : -1);
    }

    private void walkThrough(@NotNull final Player player, @NotNull final WorldObject object) {
        object.setLocked(true);
        player.resetWalkSteps();
        final WorldObject door = Door.handleGraphicalDoor(object, null);
        player.addWalkSteps(object.getX() + (player.getX() == object.getX() ? -1 : 1), player.getY(), 1, false);
        WorldTasksManager.schedule(() -> {
            Door.handleGraphicalDoor(door, object);
            object.setLocked(false);
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34840 };
    }

}
