package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Savions.
 */
public class VenatorShard extends ItemPlugin {

	@Override public void handle() {
		bind("Combine", (player, item, container, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override public void buildDialogue() {
					if (item.getAmount() < 5) {
						item(item, "Creating a venator bow requires five venator shards. You need " + (5 - item.getAmount()) + " more.");
					} else {
						plain("Do you wish to use five venator shards to create a venator bow?<br>This process is non-reversible.");
						options("Do you wish to create a venator bow?", new DialogueOption("Yes", () -> {
							player.getInventory().ifDeleteItem(new Item(ItemId.VENATOR_SHARD, 5), () -> {
								player.getDialogueManager().finish();
								final Item bow = new Item(ItemId.VENATOR_BOW_UNCHARGED);
								player.getInventory().addItem(bow);
								player.getDialogueManager().start(new ItemChat(player, bow, "You use five venator shards to create a venator bow."));
							});
						}), new DialogueOption("No."));
					}
				}
			});
		});
	}

	@Override public int[] getItems() {
		return new int[] { ItemId.VENATOR_SHARD };
	}
}
