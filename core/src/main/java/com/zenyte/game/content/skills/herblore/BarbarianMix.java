package com.zenyte.game.content.skills.herblore;

import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.ActionManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

/**
 * @author Kris | 28. aug 2018 : 22:18:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BarbarianMix implements ItemOnItemAction {

	private enum Mix {
		ATTACK_MIX(123, 11429, 4, 8), ANTIPOISON_MIX(177, 11433, 6, 12), RELICYMS_MIX(4846, 11437, 9, 14), STRENGTH_MIX(117, 11443, 14, 17), RESTORE_MIX(129, 11449, 24, 21), ENERGY_MIX(3012, 11453, 29, 23), DEFENCE_MIX(135, 11457, 33, 25), AGILITY_MIX(3036, 11461, 37, 27), COMBAT_MIX(9743, 11445, 40, 28), PRAYER_MIX(141, 11465, 42, 29), SUPERATTACK_MIX(147, 11469, 47, 33), ANTIPOISON_SUPERMIX(183, 11473, 51, 35), FISHING_MIX(153, 11477, 53, 38), SUPER_ENERGY_MIX(3020, 11481, 56, 39), HUNTING_MIX(10002, 11517, 58, 40), SUPER_STR_MIX(159, 11485, 59, 42), MAGIC_ESSENCE_MIX(9023, 11489, 61, 43), SUPER_RESTORE_MIX(3028, 11493, 67, 48), SUPER_DEF_MIX(165, 11497, 71, 50), ANTIDOTE_PLUS_MIX(5947, 11501, 74, 52), ANTIFIRE_MIX(2456, 11505, 75, 53), RANGING_MIX(171, 11509, 80, 54), MAGIC_MIX(3044, 11513, 83, 57), ZAMORAK_MIX(191, 11521, 85, 58), STAMINA_MIX(12629, 12633, 86, 60), EXTENDED_ANTIFIRE_MIX(11955, 11960, 91, 61), SUPER_ANTIFIRE_MIX(21984, 21994, 98, 70), EXTENDED_SUPER_ANTIFIRE_MIX(22215, 22221, 99, 78);
		private static final Mix[] VALUES = values();
		private static final Int2ObjectOpenHashMap<Mix> MAPPED_VALUES = new Int2ObjectOpenHashMap<Mix>();

		static {
			for (final BarbarianMix.Mix mix : VALUES) {
				MAPPED_VALUES.put(mix.basePotion, mix);
			}
		}

		private final int basePotion;
		private final int produce;
		private final int level;
		private final float experience;

		Mix(int basePotion, int produce, int level, float experience) {
			this.basePotion = basePotion;
			this.produce = produce;
			this.level = level;
			this.experience = experience;
		}
	}

	private static final int ROE = 11324;
	private static final int CAVIAR = 11326;
	private static final Animation BREWING_ANIMATION = new Animation(363);

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item eggs = from.getId() == ROE || from.getId() == CAVIAR ? from : to;
		final Item potion = eggs == from ? to : from;
		final BarbarianMix.Mix mix = Mix.MAPPED_VALUES.get(potion.getId());
		final ActionManager actionManager = player.getActionManager();
		if (mix == null || actionManager.getActionDelay() > 0) {
			return;
		}
		final Skills skills = player.getSkills();
		if (skills.getLevel(SkillConstants.HERBLORE) < mix.level) {
			player.sendMessage("You need a Herblore level of at least " + mix.level + " to combine the potion with the fish eggs.");
			return;
		}
		actionManager.setActionDelay(actionManager.getActionDelay() + 2);
		final Inventory inventory = player.getInventory();
		inventory.deleteItem(eggs);
		inventory.deleteItem(potion);
		inventory.addItem(new Item(mix.produce));
		player.setAnimation(BREWING_ANIMATION);
		skills.addXp(SkillConstants.HERBLORE, mix.experience);
		if (mix == Mix.RANGING_MIX) {
			SherlockTask.CREATE_RANGING_MIX.progress(player);
		}
		player.sendMessage("You combine your potion with the fish eggs.");
	}

	@Override
	public int[] getItems() {
		return null;
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>(Mix.VALUES.length + 6);
		for (final BarbarianMix.Mix mix : Mix.VALUES) {
			if (mix.level <= 24) {
				list.add(new ItemPair(ROE, mix.basePotion));
			}
			list.add(new ItemPair(CAVIAR, mix.basePotion));
		}
		return list.toArray(new ItemPair[list.size()]);
	}
}
