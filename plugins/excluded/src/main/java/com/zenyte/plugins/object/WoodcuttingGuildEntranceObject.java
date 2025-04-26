package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.DoubleDoor;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 6 jun. 2018 | 17:10:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WoodcuttingGuildEntranceObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean inside = player.inArea("Woodcutting Guild");
        if (!inside && player.getSkills().getLevel(SkillConstants.WOODCUTTING) < 60) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Woodcutting level of at least 60 to enter the guild."));
            return;
        }
        final DoubleDoor gate = DoubleDoor.handleGraphicalDoubleDoor(player, object, null);
        final int objX = object.getX();
        final int objY = object.getY();
        player.lock();
        player.setRunSilent(true);
        player.addWalkSteps(objX, objY, -1, false);
        player.addWalkSteps(objX + (inside ? (objX == 1562 ? 0 : 1) : (objX == 1562 ? 1 : 0)), objY, -1, false);
        WorldTasksManager.schedule(() -> {
            player.unlock();
            player.setRunSilent(false);
            DoubleDoor.handleGraphicalDoubleDoor(player, object, gate);
        }, player.getWalkSteps().size());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_28851, ObjectId.GATE_28852 };
    }
}
