package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.chambersofxeric.skills.RaidHerblore;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;

/**
 * @author Kris | 2. mai 2018 : 23:06:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class RaidHerbloreMixingIOIA implements PairedItemOnItemPlugin {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final RaidHerblore.RaidHerb fromHerb = CollectionUtils.findMatching(RaidHerblore.RaidHerb.values, value -> value.getClean() == from.getId());
		final RaidHerblore.RaidHerb toHerb = CollectionUtils.findMatching(RaidHerblore.RaidHerb.values, value -> value.getClean() == to.getId());
		if (fromHerb == null && toHerb == null) {
			player.sendMessage("You should mix your herb in before adding any other ingredients.");
			return;
		}
		final Item[] potions = RaidHerblore.RaidPotion.getPotions(player, from, to);
		if (potions.length == 0) {
			player.sendMessage("You haven't got the ingredients to make any potions.");
			return;
		}
		player.getDialogueManager().start(new RaidHerblore.RaidMixDialogue(player, potions));
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>();
		for (final RaidHerblore.RaidPotion potion : RaidHerblore.RaidPotion.values()) {
			//Only overloads have more than one secondary ingredient.
			if (potion.getSecondaryIngredient().length > 1) {
				for (int secondary : potion.getSecondaryIngredient()) {
					list.add(ItemPair.of(potion.getHerb().getClean(), secondary));
				}
				continue;
			}
			list.add(ItemPair.of(potion.getHerb().getClean(), 20801));
			for (int secondary : potion.getSecondaryIngredient()) {
				list.add(ItemPair.of(20801, secondary));
			}
		}
		return list.toArray(new ItemPair[0]);
	}
}
