package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 20/05/2019 | 17:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class GrappleTipOnRopeAction implements ItemOnItemAction {
    private static final Item MITH_GRAPPLE = new Item(9419);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item grapple = from.getId() == 9418 ? from : to;
        final Item rope = from.getId() == 954 ? from : to;
        player.getInventory().deleteItemsIfContains(new Item[] {grapple, rope}, () -> {
            player.getInventory().addItem(MITH_GRAPPLE);
            player.sendMessage("You attach a rope to the mithril grapple.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {9418, 954};
    }
}
