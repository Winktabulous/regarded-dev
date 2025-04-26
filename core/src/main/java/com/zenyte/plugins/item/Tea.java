package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.itemonitem.GuthixRestTeaMixing;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 14/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Tea extends ItemPlugin {
    @Override
    public void handle() {
        bind("Empty", (player, item, container, slotId) -> player.getInventory().set(slotId, new Item(ItemId.EMPTY_CUP)));
    }

    @Override
    public int[] getItems() {
        final IntArrayList itemIds = new IntArrayList();
        for (final GuthixRestTeaMixing.PartialTea tea : GuthixRestTeaMixing.PartialTea.values()) {
            itemIds.add(tea.getTeaId());
        }
        itemIds.rem(ItemId.GUTHIX_REST3);
        return itemIds.toIntArray();
    }
}
