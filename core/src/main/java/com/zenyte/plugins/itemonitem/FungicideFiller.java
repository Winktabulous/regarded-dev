package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 03/05/2019 15:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FungicideFiller implements PairedItemOnItemPlugin {
    private static final int FILLER = 7432;

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item filler = from.getId() == FILLER ? from : to;
        final Item can = filler == from ? to : from;
        if (can.getId() == 7421) {
            player.sendMessage("The spray cannot hold any more fungicide.");
            return;
        }
        final Inventory inventory = player.getInventory();
        inventory.deleteItem(filler);
        inventory.set(can == from ? fromSlot : toSlot, new Item(7421));
        player.sendMessage("You fill the spray with fungicide.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(FILLER, 7421), ItemPair.of(FILLER, 7422), ItemPair.of(FILLER, 7423), ItemPair.of(FILLER, 7424), ItemPair.of(FILLER, 7425), ItemPair.of(FILLER, 7426), ItemPair.of(FILLER, 7427), ItemPair.of(FILLER, 7428), ItemPair.of(FILLER, 7429), ItemPair.of(FILLER, 7430), ItemPair.of(FILLER, 7431)};
    }
}
