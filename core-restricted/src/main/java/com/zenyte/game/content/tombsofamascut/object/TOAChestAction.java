package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Savions
 */
public class TOAChestAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getTOAManager().getRewardContainer() == null || player.getTOAManager().getRewardContainer().isEmpty()) {
            player.getDialogueManager().start(new PlainChat(player, "There is nothing to claim"));
            player.getVarManager().sendBit(14139, 0);
            return;
        }
        GameInterface.TOA_LOOT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {46082, 46217, 44545, 46215, 46219, 46218, 44547, 29994, 46216, 46224};
    }
}
