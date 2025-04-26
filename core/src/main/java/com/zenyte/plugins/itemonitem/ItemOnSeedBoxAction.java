package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 3-3-2019 | 15:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ItemOnSeedBoxAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item item = from.getId() != 13639 ? from : to;
        final int itemSlot = from.getId() != 13639 ? fromSlot : toSlot;
        if (!FarmingProduct.getSeeds().contains(item.getId())) {
            player.sendMessage("You can only store seeds in the seed box.");
            return;
        }
        player.getSeedBox().getContainer().deposit(player, player.getInventory().getContainer(), itemSlot, item.getAmount());
        player.getInventory().refreshAll();
    }

    @Override
    public boolean allItems() {
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {13639};
    }
}
