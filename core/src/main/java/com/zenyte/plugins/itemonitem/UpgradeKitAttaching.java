package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.enums.UpgradeKit;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 07/05/2019 20:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UpgradeKitAttaching implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        UpgradeKit dis = null;
        final int f = from.getId();
        final int t = to.getId();
        for (final UpgradeKit val : UpgradeKit.values) {
            final int base = val.getBaseItem();
            final int kit = val.getKit();
            if (base == f && kit == t || base == t && kit == f) {
                dis = val;
                break;
            }
        }
        if (dis == null) {
            return;
        }
        player.sendMessage("You attach the " + ItemDefinitions.getOrThrow(dis.getKit()).getName().toLowerCase() + " onto the " + ItemDefinitions.getOrThrow(dis.getBaseItem()).getName().toLowerCase() + ".");
        player.getInventory().deleteItem(fromSlot, from);
        player.getInventory().deleteItem(toSlot, to);
        player.getInventory().addItem(dis.getCompleteItem(), 1);
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final UpgradeKit val : UpgradeKit.values) {
            list.add(val.getKit());
            list.add(val.getBaseItem());
        }
        return list.toArray(new int[0]);
    }

}
