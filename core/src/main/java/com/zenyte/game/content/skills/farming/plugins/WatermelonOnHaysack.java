package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 22/05/2019 00:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WatermelonOnHaysack implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getInventory().deleteItem(from);
        player.getInventory().deleteItem(to);
        player.getInventory().addOrDrop(new Item(6059));
        player.sendMessage("You put the watermelon on the spear.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(5982, 6058)
        };
    }
}
