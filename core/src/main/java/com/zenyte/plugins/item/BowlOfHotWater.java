package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 14/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BowlOfHotWater extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Empty", (player, item, container, slotId) -> player.getInventory().set(slotId, new Item(ItemId.BOWL)));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item cup = from.getId() == ItemId.EMPTY_CUP ? from : to;
        final Item bowl = cup == from ? to : from;
        final Inventory inventory = player.getInventory();
        inventory.set(cup == from ? fromSlot : toSlot, new Item(ItemId.CUP_OF_HOT_WATER));
        inventory.set(bowl == from ? fromSlot : toSlot, new Item(ItemId.BOWL));
        player.sendFilteredMessage("You pour the hot water into the tea cup.");
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.BOWL_OF_HOT_WATER};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(ItemId.EMPTY_CUP, ItemId.BOWL_OF_HOT_WATER)};
    }
}
