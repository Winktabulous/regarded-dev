package com.zenyte.game.content.treasuretrails.clues.emote;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SingleItemRequirement implements ItemRequirement {
    private final int itemId;

    public SingleItemRequirement(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean fulfilledBy(int itemId) {
        return this.itemId == itemId;
    }

    @Override
    public boolean fulfilledBy(Item[] items) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IntArrayList getFulfillingItemsIndexes(Item[] items) {
        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            if (item == null) {
                continue;
            }
            if (item.getId() == itemId) {
                return new IntArrayList(new int[] {i});
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "SingleItemRequirement(itemId=" + this.itemId + ")";
    }
}
