package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 14/06/2019 15:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TotemCreation implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Inventory inv = player.getInventory();
        if (!(inv.containsItem(19679, 1) && inv.containsItem(19681, 1) && inv.containsItem(19683, 1))) {
            player.getDialogueManager().start(new ItemChat(player, new Item(19685), "You need all three pieces of the totem to assemble it."));
            return;
        }
        inv.deleteItem(19679, 1);
        inv.deleteItem(19681, 1);
        inv.deleteItem(19683, 1);
        inv.addItem(19685, 1);
        player.getDialogueManager().start(new ItemChat(player, new Item(19685), "You assemble the three totem pieces to make a dark totem."));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(19679, 19681), ItemPair.of(19679, 19683), ItemPair.of(19681, 19683)};
    }
}
