package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FillContainer;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

public class CowObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.toLowerCase().equals("milk")) {
            if (player.getInventory().containsItem(1925, 1))
                player.getActionManager().setAction(new FillContainer(object, new Item(1925)));
            else
                player.getDialogueManager().start(new PlainChat(player, "You need a bucket to milk this cow."));
            return;
        }
        if (option.toLowerCase().equals("steal-cowbell")) {
            player.getDialogueManager().start(new PlainChat(player, "You have no reason to steal her cowbell.."));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.DAIRY_COW, ObjectId.DAIRY_COW_12111};
    }

}
