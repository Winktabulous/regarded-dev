package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14/06/2019 11:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CoconutCracking implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item hammer = from.getId() == 2347 ? from : to;
        final Item coconut = from == hammer ? to : from;
        player.getInventory().deleteItem(coconut);
        player.getInventory().addOrDrop(new Item(5976));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(2347, 5974)};
    }
}
