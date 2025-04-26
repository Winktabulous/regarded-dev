package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.GemBag;

/**
 * @author Tommeh | 13 jul. 2018 | 21:28:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GemOnGemBagItemAction implements ItemOnItemAction {
    private static final int GEM_BAG_ID = com.zenyte.game.model.item.containers.GemBag.GEM_BAG.getId();

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (from.getId() != GEM_BAG_ID && to.getId() != GEM_BAG_ID) {
            return; // no gem bag being used in the action
        }
        final com.zenyte.game.model.item.containers.GemBag bag = player.getGemBag();
        final Item gem = from.getId() != GEM_BAG_ID ? from : to;
        final int gemSlot = from.getId() != GEM_BAG_ID ? fromSlot : toSlot;
        final int amount = bag.getAmountOf(gem.getId());
        final int size = bag.getSize();
        final String name = GemBag.GEMS.get(gem.getId()).toLowerCase();
        if (size == 300) {
            player.sendMessage("You cannot hold more than 300 gems in your gem bag.");
            return;
        }
        if (amount == 60) {
            player.sendMessage("You cannot store anymore " + name + ".");
            return;
        }
        bag.getContainer().deposit(player, player.getInventory().getContainer(), gemSlot, 1);
        player.getInventory().refreshAll();
    }

    @Override
    public int[] getItems() {
        return new int[] {1617, 1619, 1621, 1623, 1631, GEM_BAG_ID};
    }
}
