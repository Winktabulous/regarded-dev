package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.itemonitem.SerpentineHelmetChargingAction;

/**
 * @author Kris | 25. aug 2018 : 22:45:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SerpentineHelm extends ItemPlugin {
	private static final Item CHARGED_HELMET = new Item(12931);
	private static final Item UNCHARGED_HELMET = new Item(12929);
	private static final Item UNCHARGED_TANZANITE_HELMET = new Item(13196);
	private static final Item UNCHARGED_MAGMA_HELMET = new Item(13198);

	@Override
	public void handle() {
		bind("Restore", (player, item, slotId) -> {
			final boolean charged = !item.getName().contains("(uncharged)");
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(item, "Restoring the " + item.getName().toLowerCase() + " will <col=800000>not</col> grant you back the mutagen, only the serpentine helmet.");
					options("Do you wish to restore the " + item.getName().toLowerCase() + "?", "Yes", "No").onOptionOne(() -> {
						setKey(5);
						player.getInventory().set(slotId, new Item(charged ? CHARGED_HELMET.getId() : UNCHARGED_HELMET.getId(), 1, charged ? item.getCharges() : 0));
					});
					item(5, CHARGED_HELMET, "The " + item.getName().toLowerCase() + " was successfully restored to its original state.");
				}
			});
		});
		bind("Uncharge", (player, item, slotId) -> {
			final Item replacement = item.getId() == 12931 ? UNCHARGED_HELMET : item.getId() == 13197 ? UNCHARGED_TANZANITE_HELMET : UNCHARGED_MAGMA_HELMET;
			if (!player.getInventory().hasFreeSlots()) {
				player.sendFilteredMessage("Not enough space your in inventory.");
				return;
			}
			player.getInventory().set(slotId, new Item(replacement));
			player.getInventory().addItem(12934, (int) (item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO));
			player.sendMessage("You uncharge your serpentine helmet.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {12931, 13196, 13197, 13198, 13199};
	}
}
