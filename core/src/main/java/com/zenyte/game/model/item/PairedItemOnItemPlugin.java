package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;

/**
 * @author Kris | 5. sept 2018 : 02:10:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface PairedItemOnItemPlugin extends ItemOnItemAction {
	@Override
	default int[] getItems() {
		return null;
	}

	@Override
	ItemPair[] getMatchingPairs();

	default ItemPair getMatchingPair(final Item from, final Item to, final ItemPair... itemPairs) {
		for (int i = 0, len = itemPairs.length; i < len; i++) {
			final ItemOnItemAction.ItemPair pair = itemPairs[i];
			if (matches(pair, from, to)) {
				return pair;
			}
		}
		throw new RuntimeException("Unable to locate an item pair for items " + from + " and " + to + ".");
	}

	default int getMatchingPairIndex(final Item from, final Item to, final ItemPair... itemPairs) {
		for (int i = 0, len = itemPairs.length; i < len; i++) {
			final ItemOnItemAction.ItemPair pair = itemPairs[i];
			if (matches(pair, from, to)) {
				return i;
			}
		}
		throw new RuntimeException("Unable to locate an item pair for items " + from + " and " + to + ".");
	}

	default ItemPair[] concatenate(final ItemPair[] array, final ItemPair... itemPairs) {
		final ItemOnItemAction.ItemPair[] pairs = new ItemPair[array.length + itemPairs.length];
		System.arraycopy(array, 0, pairs, 0, array.length);
		System.arraycopy(itemPairs, 0, pairs, array.length, itemPairs.length);
		return pairs;
	}

	default boolean matches(final ItemPair pair, final Item from, final Item to) {
		final int fromId = from.getId();
		final int toId = to.getId();
		return fromId == pair.left && toId == pair.right || fromId == pair.right && toId == pair.left;
	}
}
