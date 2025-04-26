package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

@SuppressWarnings("unused")
public class ThreadOfElidinisOnRunePouch implements PairedItemOnItemPlugin {

	public static final Item THREAD_OF_ELIDINIS = new Item(ItemId.THREAD_OF_ELIDINIS);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 75) {
			player.sendMessage("You need a Crafting level of at least 75 to augment your rune pouch.");
			return;
		}

		if (!player.getInventory().containsItem(CraftingDefinitions.NEEDLE)) {
			player.sendMessage("You need a needle to work with the Thread of Elidinis.");
			return;
		}

		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				doubleItem(RunePouch.RUNE_POUCH, THREAD_OF_ELIDINIS, "You are attempting to use the " + Colour.DARK_BLUE.wrap("Thread of Elidinis to augment your rune pouch. This process is reversible at any time and thread will be returned."));
				options("Augment your rune pouch?", new DialogueOption("Yes.", () -> {
					if (!player.getInventory().containsItem(from) || !player.getInventory().containsItem(to)) {
						return;
					}

					player.lock();
					player.setAnimation(ChiselOnArmadyl.ANIM);
					player.delay(2, () -> {
						player.getInventory().deleteItem(from);
						player.getInventory().deleteItem(to);
						player.getInventory().addItem(RunePouch.DIVINE_RUNE_POUCH);
						player.getDialogueManager().item(RunePouch.DIVINE_RUNE_POUCH, "You skillfully weave the Thread of Elidinis into the rune pouch and watch as it transforms into something more.");
						player.unlock();
					});
				}), new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[]{ItemPair.of(ItemId.RUNE_POUCH, ItemId.THREAD_OF_ELIDINIS)};
	}

}
