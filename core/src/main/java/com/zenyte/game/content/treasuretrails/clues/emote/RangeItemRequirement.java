package com.zenyte.game.content.treasuretrails.clues.emote;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RangeItemRequirement implements ItemRequirement {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String name;
    private final int startItemId;
    private final int endItemId;

    public RangeItemRequirement(String name, int startItemId, int endItemId) {
        this.name = name;
        this.startItemId = startItemId;
        this.endItemId = endItemId;
    }

    @Override
    public boolean fulfilledBy(int itemId) {
        return itemId >= startItemId && itemId <= endItemId;
    }

    @Override
    public boolean fulfilledBy(Item[] items) {
        for (Item item : items) {
            if (item.getId() >= startItemId && item.getId() <= endItemId) {
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
            if (item.getId() >= startItemId && item.getId() <= endItemId) {
                return new IntArrayList(new int[] {i});
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RangeItemRequirement(name=" + this.name + ", startItemId=" + this.startItemId + ", endItemId=" + this.endItemId + ")";
    }
}
