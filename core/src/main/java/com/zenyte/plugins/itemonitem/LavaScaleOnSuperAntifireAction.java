package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Tommeh | 12 jun. 2018 | 14:38:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LavaScaleOnSuperAntifireAction implements ItemOnItemAction {
	
	public static final Int2IntOpenHashMap POTS = new Int2IntOpenHashMap(new int[] { 21987, 21984, 21981, 21978 }, new int[] { 22218, 22215, 22212, 22209 });

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (player.getSkills().getLevel(SkillConstants.HERBLORE) < 98) {
			player.sendMessage("You need a Herblore level of at least 98 to brew extended super antifire potions.");
			return;
		}
		final Item potion = from.getId() >= 21978 && from.getId() <= 21987 ? from : to;
		final int slot = player.getInventory().getItem(fromSlot).getId() == potion.getId() ? fromSlot : toSlot;
		final int dose = Integer.valueOf(potion.getName().substring(22, 23));
		if (!player.getInventory().containsItem(11994, dose)) {
			player.sendMessage("You don't have enough lava scale shards.");
			return;
		}
		player.getInventory().deleteItem(11994, dose);
		player.getInventory().set(slot, new Item(POTS.get(potion.getId())));
		player.getSkills().addXp(SkillConstants.HERBLORE, 40 * dose);
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				ItemPair.of(21978, 11994), ItemPair.of(21981, 11994),
				ItemPair.of(21984, 11994), ItemPair.of(21987, 11994)
		};
	}

	@Override
	public int[] getItems() {
		return null;
	}

}
