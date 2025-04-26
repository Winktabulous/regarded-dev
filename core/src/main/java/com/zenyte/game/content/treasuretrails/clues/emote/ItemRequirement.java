package com.zenyte.game.content.treasuretrails.clues.emote;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface ItemRequirement {
    boolean fulfilledBy(int itemId);

    boolean fulfilledBy(Item[] items);

    IntArrayList getFulfillingItemsIndexes(final Item[] items);
}
