package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Tommeh | 12 jun. 2018 | 14:03:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ZulrahScaleOnAntidoteItemAction implements ItemOnItemAction {
	public static final Int2IntOpenHashMap POTS = new Int2IntOpenHashMap(new int[] {5958, 5956, 5954, 5952}, new int[] {12911, 12909, 12907, 12905});

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (player.getSkills().getLevel(SkillConstants.HERBLORE) < 87) {
			player.sendMessage("You need a Herblore level of at least 87 to brew anti-venom potions.");
			return;
		}
		final Item potion = from.getId() <= 5952 ? from : to;
		final int slot = player.getInventory().getItem(fromSlot).getId() == potion.getId() ? fromSlot : toSlot;
		final Integer dose = Integer.valueOf(potion.getName().substring(11, 12));
		if (!player.getInventory().containsItem(12934, dose * 5)) {
			player.sendMessage("You don't have enough zulrah's scales.");
			return;
		}
		player.getAchievementDiaries().update(KaramjaDiary.CREATE_AN_ANTIVENOM_POTION);
		player.getInventory().deleteItem(12934, dose * 5);
		player.getInventory().set(slot, new Item(POTS.get(potion.getId())));
		player.getSkills().addXp(SkillConstants.HERBLORE, dose * 30);
		SherlockTask.MIX_ANTIVENOM.progress(player);
	}

	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {ItemPair.of(5952, 12934), ItemPair.of(5954, 12934), ItemPair.of(5956, 12934), ItemPair.of(5958, 12934)};
	}

	@Override
	public int[] getItems() {
		return null;
	}
}
