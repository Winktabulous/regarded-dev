package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class PestControlShipsLadder implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new NPCChat(player, 1769, "Hey! You can't go there."));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_287 };
    }
}
