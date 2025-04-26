package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Chris
 * @since August 18 2020
 */
public class TorstolOnAntiVenom1To3 implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.sendMessage("The torstol must be added to a 4-dose vial.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM1),
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM2),
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM3)
        };
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
