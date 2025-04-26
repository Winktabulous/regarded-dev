package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.actions.FillVialAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public final class GauntletWaterPump implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new FillVialAction());
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new FillVialAction());
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 23879 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 36078, 35981 };
    }

}
