package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * Handles the charging of an ash sanctifier by using death runes on it.
 */
@SuppressWarnings("unused")
public final class AshSanctifierItemAction implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {

        final Item sanctifierItem;
        final int deathRuneCount;

        if (from.getId() == ItemId.DEATH_RUNE) {
            deathRuneCount = from.getAmount();
            sanctifierItem = to;
        } else {
            deathRuneCount = to.getAmount();
            sanctifierItem = from;
        }

        final int currentCharges = sanctifierItem.getCharges();
        try {
            sanctifierItem.setCharges(currentCharges + deathRuneCount * 10);
            player.sendMessage("You place " + deathRuneCount + " into your Ash sanctifier.");
            player.sendMessage("Your Ash sanctifier has " + sanctifierItem.getCharges() + " charges left.");
            player.getInventory().deleteItem(new Item(ItemId.DEATH_RUNE, deathRuneCount));
        } catch (Exception ex) {
            player.sendMessage("You can't add anymore to your ash sanctifier");
        }
    }

    @Override
    public int[] getItems() {
        return new int[]{ ItemId.ASH_SANCTIFIER, ItemId.DEATH_RUNE };
    }
}
