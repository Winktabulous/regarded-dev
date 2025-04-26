package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 26. aug 2018 : 23:43:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BagFullOfGems extends ItemPlugin {

	private enum Gem {
		UNCUT_SAPPHIRE(1624, 49.91F), UNCUT_EMERALD(1622, 34.62F), UNCUT_RUBY(1620, 11.82F), UNCUT_DIAMOND(1618, 3.09F), UNCUT_DRAGONSTONE(1632, 0.56F), UNCUT_ONYX(6572, 1.0E-6F);
		private static final Gem[] VALUES = values();

		static {
			ArrayUtils.reverse(VALUES);
		}

		private final int notedId;
		private final float percentage;

		Gem(int notedId, float percentage) {
			this.notedId = notedId;
			this.percentage = percentage;
		}
	}

	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
			final Inventory inventory = player.getInventory();
			int spaceRequired = 5;
			for (final BagFullOfGems.Gem gem : Gem.VALUES) {
				if (inventory.containsItem(gem.notedId, 1)) {
					spaceRequired--;
				}
			}
			if (inventory.getFreeSlots() < spaceRequired) {
				player.sendMessage("You need some more free space to open the bag.");
				return;
			}
			inventory.deleteItem(item);
			final Int2IntOpenHashMap map = new Int2IntOpenHashMap(6);
			for (int i = 0; i < 40; i++) {
				final double roll = Utils.getRandomDouble(100);
				float percentage = 0;
				for (final BagFullOfGems.Gem gem : Gem.VALUES) {
					if (roll <= (percentage += gem.percentage)) {
						map.put(gem.notedId, map.get(gem.notedId) + 1);
						break;
					}
				}
			}
			final ObjectIterator<Int2IntMap.Entry> iterator = map.int2IntEntrySet().fastIterator();
			while (iterator.hasNext()) {
				final Int2IntMap.Entry next = iterator.next();
				final int id = next.getIntKey();
				final int amount = next.getIntValue();
				inventory.addItem(new Item(id, amount)).onFailure(i -> World.spawnFloorItem(i, player));
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {19473};
	}
}
