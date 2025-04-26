package com.near_reality.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

public class HolyItemCreation implements PairedItemOnItemPlugin {

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final BossDropItem item = BossDropItem.getItemByMaterials(from, to);
		if (item == null) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}

		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				item(item.getItem(), "You're about to attach an Angelic artifact to this weapon. You'll also need 2,500 Degraded essence.");
				options("Attach the kit to this item?", new DialogueOption("Yes.", () -> player.getInventory().deleteItemsIfContains(item.getMaterials(), () -> {
					player.getInventory().addOrDrop(item.getItem());
					player.getDialogueManager().start(new ItemChat(player, item.getItem(), "You successfully combine all the materials into one."));
				})), new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				ItemPair.of(BossDropItem.HOLY_GREAT_HAMMER.getMaterials()[0], BossDropItem.HOLY_GREAT_HAMMER.getMaterials()[1]),
				ItemPair.of(BossDropItem.HOLY_GREAT_LANCE.getMaterials()[0], BossDropItem.HOLY_GREAT_LANCE.getMaterials()[1])
		};
	}

}
