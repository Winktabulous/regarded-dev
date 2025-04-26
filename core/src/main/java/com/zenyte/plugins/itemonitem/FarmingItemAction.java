package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.farming.Seedling;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:15.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class FarmingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (from.getId() == 5354 || to.getId() == 5354) {
			if (!player.getInventory().containsItem(5325, 1)) {
				player.sendMessage("You need a gardening trowel to do this.");
				return;
			}
			final Item seed = from.getId() == 5354 ? to : from;
			final Item pot = seed == from ? to : from;
			if (seed.getName().contains("seedling")) {
				return;
			}
			final Seedling seedling = Seedling.getSeedling(seed.getId());
			if (seedling == null) {
				player.sendMessage("You can't place this in a plant pot.");
				return;
			}
			player.getInventory().deleteItem(seed.getId(), 1);
			player.getInventory().deleteItem(pot);
			player.getInventory().addItem(new Item(seedling.getSeedling(), 1));
			player.sendMessage("You put the seed in the soil.");
		} else if ((from.getId() >= 5333 && from.getId() <= 5340) || (to.getId() >= 5333 && to.getId() <= 5340) || from.getId() == 13353 || to.getId() == 13353) {
			final Item seed = from.getId() == 13353 || (from.getId() >= 5333 && from.getId() <= 5340) ? to : from;
			final Item can = seed == from ? to : from;
			final Seedling seedling = Seedling.getWaterableSeedling(seed.getId());
			if (seedling == null) {
				player.sendMessage("You can't water this.");
				return;
			}
			if (can.getId() != 13353) {
				can.setId(can.getId() == 5333 ? 5331 : (can.getId() - 1));
			}
			seed.setId(seedling.getWateredSeedling());
			player.getInventory().refreshAll();
			player.getVariables().schedule(15, TickVariable.SEEDLING_SPROUT);
			player.sendFilteredMessage("You water the seedling.");
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		list.add(5354);
		list.add(13353);
		for (int i = 5333; i <= 5340; i++) {
			list.add(i);
		}
		list.addAll(Seedling.getIngredientMap().keySet());
		return list.toIntArray();
	}
}
