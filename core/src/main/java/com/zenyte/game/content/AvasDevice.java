package com.zenyte.game.content;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 31. dets 2017 : 1:43.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AvasDevice {

	private static final ImmutableItem[] ATTRACTOR_POSSIBLE_LOOT = new ImmutableItem[] { 
		new ImmutableItem(884, 1, 3, 20),
		new ImmutableItem(440, 1, 1, 8),
		new ImmutableItem(1137, 1, 1, 8),
		new ImmutableItem(1153, 1, 1, 8),
		new ImmutableItem(807, 1, 2, 8),
		new ImmutableItem(863, 1, 3, 8),
		new ImmutableItem(2351, 1, 1, 9),
		new ImmutableItem(9140, 1, 3, 10),
		new ImmutableItem(40, 1, 4, 10),
		new ImmutableItem(1141, 1, 1, 5),
		new ImmutableItem(1157, 1, 1, 5),
		new ImmutableItem(7767, 1, 1, 1),
	};
	
	private static final ImmutableItem[] ACCUMULATOR_POSSIBLE_LOOT = new ImmutableItem[] { 
			new ImmutableItem(886, 1, 3, 15),
			new ImmutableItem(687, 1, 1, 6),
			new ImmutableItem(440, 1, 1, 6),
			new ImmutableItem(1137, 1, 1, 6),
			new ImmutableItem(1153, 1, 1, 6),
			new ImmutableItem(808, 1, 2, 6),
			new ImmutableItem(865, 1, 3, 6),
			new ImmutableItem(2353, 1, 1, 6),
			new ImmutableItem(9141, 1, 3, 6),
			new ImmutableItem(41, 1, 4, 6),
			new ImmutableItem(1141, 1, 1, 6),
			new ImmutableItem(1157, 1, 1, 6),
			new ImmutableItem(1353, 1, 1, 6),
			new ImmutableItem(1539, 1, 4, 6),
			new ImmutableItem(1119, 1, 1, 6),
			new ImmutableItem(7767, 1, 1, 1),
		};
	
	public static final void collectMetal(final Player player) {
		final Object object = player.getAttributes().get("avasDeviceRetrieve");
		if (object == null)
			return;
		final int id = player.getCape() == null ? -1 : player.getCape().getId();
		if (id != 10498 && id != 10499 && id != 13337 && id != 22109 && id != 21898)
			return;
		final Item body = player.getChest();
		final long time = player.getNumericTemporaryAttribute("avaRetrieveDelay").longValue();
		if (time == 0) {
			player.getTemporaryAttributes().put("avaRetrieveDelay", Utils.currentTimeMillis() + Utils.random(200000, 300000));
			return;
		} else if (time > Utils.currentTimeMillis())
			return;
		player.getTemporaryAttributes().put("avaRetrieveDelay", Utils.currentTimeMillis() + Utils.random(200000, 300000));
		if (body != null && (body.getName().contains("platebody") || body.getName().contains("chainbody"))) {
			final int[] bonuses = body.getDefinitions().getBonuses();
			if (bonuses != null && bonuses[4] < 0) {
				player.sendMessage("Your armour interferes with Ava's device.");
				return;
			}
		}
		final int random = Utils.random(100);
		int current = 0;
		for (final ImmutableItem item : (id == 10498 ? ATTRACTOR_POSSIBLE_LOOT : ACCUMULATOR_POSSIBLE_LOOT)) {
			if ((current += (int) item.getRate()) >= random) {
				if (player.getInventory().hasFreeSlots() || player.getInventory().containsItem(item.getId(), 1) && ItemDefinitions.get(item.getId()).isStackable())
					player.getInventory().addItem(new Item(item.getId(), Utils.random(item.getMinAmount(), item.getMaxAmount())));
				else 
					World.spawnFloorItem(new Item(item.getId(), Utils.random(item.getMinAmount(), item.getMaxAmount())), player);
				player.sendMessage("Your ava's device has attracted some metal.");
				return;
			}
		}
	}
	
}
