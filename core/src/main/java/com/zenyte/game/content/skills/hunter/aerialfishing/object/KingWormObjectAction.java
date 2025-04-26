package com.zenyte.game.content.skills.hunter.aerialfishing.object;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Cresinkel
 */

public class KingWormObjectAction implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Take")) {
            final Inventory inven = player.getInventory();
            if (inven.hasFreeSlots()) {
                player.setAnimation(Animation.GRAB);
                inven.addItem(ItemId.KING_WORM, 1);
            } else {
                player.sendMessage("You can not carry anymore King Worm's.");
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.KING_WORM};
    }
}
