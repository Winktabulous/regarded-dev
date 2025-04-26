package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.boons.impl.ArcaneKnowledge;
import com.zenyte.game.content.skills.magic.Rune;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.ItemOnItemHandler;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25. aug 2018 : 22:43:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class RunePouchItem extends ItemPlugin  {

	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
		    player.stopAll();
			player.addTemporaryAttribute("rune_pouch", item.getId());
            GameInterface.RUNE_POUCH.open(player);
		});
		bind("Empty", (player, item, slotId) -> {
			final int itemId = item.getId();
			final RunePouch runePouch = RunePouch.chooseRunePouch(player, itemId);
			runePouch.emptyRunePouch();
		});
		bind("Revert", (player, item, slotId) -> {
			if (item.getId() != RunePouch.DIVINE_RUNE_POUCH.getId()) {
				return;
			}

			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("Not enough space in your inventory to revert Divine rune pouch into its original state.");
				return;
			}

			Item rune = player.getRunePouch().getRune(3);
			if (rune != null) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("Your divine rune pouch has more runes in it than a regular rune pouch can contain. If you proceed with this you will lose " + Colour.MAROON.wrap(Utils.pluralizedFormatted(rune.getName(), rune.getAmount())) + ".");
						options("Revert your divine rune pouch?", new DialogueOption("Yes.", () -> revertDivine(player, item)), new DialogueOption("No."));
					}
				});
				return;
			}

			revertDivine(player, item);
		});
		bind("Extra Slot", (player, item, slotId) -> {
			if(!player.getBoonManager().hasBoon(ArcaneKnowledge.class)) {
				player.sendMessage("You must have " + Colour.BLUE.wrap("Arcane Knowledge") + " unlocked to be do this.");
				return;
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Extra Slot"  + (hasRunes(player) ? getRuneString(player) : " (current: Empty)"),
							new DialogueOption("Remove all", () -> emptyBonusRunes(player, item)),
							new DialogueOption("Add rune", key(100)),
							new DialogueOption("Cancel")
					);
					plain(100, "To add a bonus rune, use it on the pouch in your inventory.");
				}
			});
		});
	}

	private boolean hasRunes(Player player) {
		return player.getRunePouch().bonusRuneTypeStored != null && player.getRunePouch().bonusRuneQuantityStored > 0;
	}

	private String getRuneString(Player player) {
		return " (current: " + Utils.formatNumberWithCommas(player.getRunePouch().bonusRuneQuantityStored) + " " + player.getRunePouch().bonusRuneTypeStored.name().toLowerCase() + " runes)";
	}

	private void emptyBonusRunes(Player player, Item item) {
		if(!player.getInventory().hasFreeSlots()) {
			player.sendMessage("Please clear one inventory slot to do this.");
			player.getDialogueManager().finish();
			return;
		}

		if(player.getRunePouch().bonusRuneQuantityStored > 0 && player.getRunePouch().bonusRuneTypeStored != null) {
			player.getInventory().addItem(new Item(player.getRunePouch().bonusRuneTypeStored.getId(), player.getRunePouch().bonusRuneQuantityStored));
			player.getRunePouch().bonusRuneTypeStored = null;
			player.getRunePouch().bonusRuneQuantityStored = 0;
			player.getInventory().refreshAll();
			player.getRunePouch().getContainer().refresh(player);
		} else {
			player.sendMessage("You do not have anything to remove from the bonus slot.");
		}
	}

	public static final Item THREAD_OF_ELIDINIS = new Item(ItemId.THREAD_OF_ELIDINIS);


	private static void revertDivine(final Player player, final Item item) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				if (!player.getInventory().containsItem(item)) {
					return;
				}

				player.getInventory().deleteItem(item);
				player.getInventory().addItem(THREAD_OF_ELIDINIS);
				player.getInventory().addItem(RunePouch.RUNE_POUCH);
				player.getRunePouch().getContainer().set(3, null);
				player.getRunePouch().getContainer().refresh(player);
				doubleItem(THREAD_OF_ELIDINIS, RunePouch.RUNE_POUCH, "You skillfully remove the Thread of Elidinis from the divine rune pouch reverting it to it's prior state.");
			}
		});
	}



	@Override
	public int[] getItems() {
		return new int[] { ItemId.RUNE_POUCH, ItemId.DIVINE_RUNE_POUCH, ItemId.TOURNAMENT_RUNE_POUCH };
	}

}
