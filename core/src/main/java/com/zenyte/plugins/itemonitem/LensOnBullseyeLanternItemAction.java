package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 2-1-2019 | 23:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LensOnBullseyeLanternItemAction implements ItemOnItemAction {

    private static final Item BULLSEYE_LANTERN = new Item(4548);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getInventory().deleteItems(from, to);
        player.getInventory().addItem(BULLSEYE_LANTERN);
        player.sendMessage("You attach the lens onto the lantern.");
    }

    @Override
    public int[] getItems() {
        return new int[] { 4542, 4544 };
    }
}
