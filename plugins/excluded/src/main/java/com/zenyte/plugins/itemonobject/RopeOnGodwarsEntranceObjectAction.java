package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 1 jan. 2018 : 18:00:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class RopeOnGodwarsEntranceObjectAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getBooleanAttribute(Setting.GODWARS_ENTRANCE_ROPE.toString())) {
            player.getSettings().toggleSetting(Setting.GODWARS_ENTRANCE_ROPE);
            player.getInventory().deleteItem(new Item(954));
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 954 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 26419 };
    }
}
