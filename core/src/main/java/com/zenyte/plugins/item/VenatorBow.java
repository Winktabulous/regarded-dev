package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.utilities.StringFormatUtil;

/**
 * @author Savions.
 */
public class VenatorBow extends ItemPlugin implements PairedItemOnItemPlugin {

	private static final int MAX_CHARGES = 50_000;

	@Override public void handle() {
		bind("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
		bind("Uncharge", (player, item, container, slotId) -> {
			if (item.getCharges() < 1) {
				return;
			}
			if (container.getFreeSlotsSize() <= 0 && !container.contains(ItemId.ANCIENT_ESSENCE, 1)) {
				player.sendMessage("You need some more free space to uncharge the bow.");
				return;
			}
			container.add(new Item(ItemId.ANCIENT_ESSENCE, item.getCharges()));
			item.setCharges(0);
			item.setId(ItemId.VENATOR_BOW_UNCHARGED);
			container.refresh(slotId);
		});
	}

	@Override public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final boolean toIsEssence = to.getId() == ItemId.ANCIENT_ESSENCE;
		final Item bow = toIsEssence ? from : to;
		final Item essence = toIsEssence ? to : from;
		if (bow.getCharges() == MAX_CHARGES) {
			player.sendMessage("Your bow is already fully charged.");
			return;
		}
		final int chargesToAdd = Math.min(MAX_CHARGES - bow.getCharges(), essence.getAmount());
		player.sendInputInt("How many charges do you wish to add to your venator bow? (0 - " + chargesToAdd + ")", value -> {
			if (value < 1) {
				return;
			}
			final int addedCharges = Math.min(chargesToAdd, value);
			player.getInventory().ifDeleteItem(new Item(ItemId.ANCIENT_ESSENCE, addedCharges), () -> {
				if (bow.getId() == ItemId.VENATOR_BOW_UNCHARGED) {
					bow.setId(ItemId.VENATOR_BOW);
					player.getInventory().refresh(toIsEssence ? fromSlot : toSlot);
				}
				bow.setCharges(bow.getCharges() + Math.min(value, addedCharges));
				player.getDialogueManager().start(new ItemChat(player, bow, "You use " + bow.getCharges() +
						" ancient essence to charge your venator bow. It now has " + bow.getCharges() + " charges"));
			});
		});
	}

	@Override public int[] getItems() {
		return new int[] { ItemId.VENATOR_BOW };
	}

	@Override public ItemPair[] getMatchingPairs() {
		return new ItemPair[] { ItemPair.of(ItemId.ANCIENT_ESSENCE, ItemId.VENATOR_BOW), ItemPair.of(ItemId.ANCIENT_ESSENCE, ItemId.VENATOR_BOW_UNCHARGED)};
	}
}
