package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.content.chompy.BellowsAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

public class BellowsOnSwampBubbles implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new BellowsAction());
    }

    @Override
    public Object[] getItems() {
        return ArrayUtils.toObject(ItemChain.OGRE_BELLOWS.getAllButLast());
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{SwampBubbles.SWAMP_BUBBLES_ID};
    }
}
