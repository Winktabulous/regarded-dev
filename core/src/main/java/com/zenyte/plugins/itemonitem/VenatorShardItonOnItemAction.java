package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.herblore.actions.Combine;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Savions.
 */
public class VenatorShardItonOnItemAction implements ItemOnItemAction {

	@Override public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override public void buildDialogue() {
				item(ItemId.VENATOR_SHARD, "Your venator shard will be crushed and 50.000 essence will be returned.<br>This action is not reversible.");
				options("Are you sure?", new DialogueOption("Yes", () -> {
					final Item essence = new Item(ItemId.ANCIENT_ESSENCE, 50_000);
					if (!player.getInventory().hasSpaceFor(essence)) {
						player.getDialogueManager().start(new PlainChat(player, "You don't have enough inventory space to do that."));
						return;
					}
					player.getInventory().ifDeleteItem(new Item(ItemId.VENATOR_SHARD, 1), () -> {
						player.getInventory().addItem(essence);
						player.setAnimation(Combine.grindingAnimation);
						player.getDialogueManager().start(new ItemChat(player, essence, "You grind your venator shard into 50.000 ancient essence."));
					});
				}), new DialogueOption("No"));
			}
		});
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.VENATOR_SHARD, ItemId.PESTLE_AND_MORTAR};
	}
}
