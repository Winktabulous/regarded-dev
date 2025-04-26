package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.BossTaskSumona;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 18/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DagannothKingsLadder implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
        case "Standard": 
            player.useStairs(828, new Location(2900, 4449, 0), 1, 2);
            break;
        case "Slayer": 
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || (assignment.getTask() != RegularTask.DAGANNOTH && assignment.getTask() != BossTask.DAGANNOTH_KINGS && assignment.getTask() != BossTaskSumona.DAGANNOTH_KINGS_SUMONA)) {
                player.getDialogueManager().start(new PlainChat(player, "You need to be on a dagannoths slayer task to access the slayer-only dungeon."));
                return;
            }
            player.useStairs(828, new Location(2899, 4385, 0), 1, 2);
            break;
        case "Peek": 
            player.sendMessage("You peek through the crack...");
            WorldTasksManager.schedule(() -> {
                final int playerCount = GlobalAreaManager.get("Dagannoth Kings Lair").getPlayers().size();
                final int slayerPlayerCount = GlobalAreaManager.get("Dagannoth Kings Slayer-Only Lair").getPlayers().size();
                player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
                player.sendMessage("Slayer cave: " + (slayerPlayerCount == 0 ? "No adventurers." : slayerPlayerCount + (slayerPlayerCount == 1 ? " adventurer." : " adventurers.")));
            }, 2);
            break;
        case "Private": 
            player.sendMessage(Colour.RED.wrap("You can start a private instance by interacting with the crack south-east of here. It has temporarily been marked with a hint arrow."));
            final HintArrow arrow = new HintArrow(1917, 4362, (byte) 100, HintArrowPosition.CENTER);
            player.getPacketDispatcher().sendHintArrow(arrow);
            //Reset the hint arrow 10 ticks from now.
            WorldTasksManager.schedule(() -> {
                if (player.getTemporaryAttributes().get("last hint arrow") != arrow) {
                    return;
                }
                player.getPacketDispatcher().resetHintArrow();
            }, 10);
            break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {3831};
    }
}
