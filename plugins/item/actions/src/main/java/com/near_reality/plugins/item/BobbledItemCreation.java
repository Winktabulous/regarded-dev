package com.near_reality.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

public class BobbledItemCreation implements PairedItemOnItemPlugin {

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
				item(item.getItem(), "You're about to attach the Malevolent energy to this armour set.");
				options("Attach the energy to this set?", new DialogueOption("Yes.", () -> player.getInventory().deleteItemsIfContains(item.getMaterials(), () -> {
					player.getCollectionLog().add(item.getItem());
					player.getInventory().addOrDrop(item.getItem());
					player.getDialogueManager().start(new ItemChat(player, item.getItem(), "You successfully combine all the materials into a pet."));
				})), new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				ItemPair.of(BossDropItem.AHRIM_THE_BOBBLED.getMaterials()[0], BossDropItem.AHRIM_THE_BOBBLED.getMaterials()[1]),
				ItemPair.of(BossDropItem.DHAROK_THE_BOBBLED.getMaterials()[0], BossDropItem.DHAROK_THE_BOBBLED.getMaterials()[1]),
				ItemPair.of(BossDropItem.GUTHAN_THE_BOBBLED.getMaterials()[0], BossDropItem.GUTHAN_THE_BOBBLED.getMaterials()[1]),
				ItemPair.of(BossDropItem.KARIL_THE_BOBBLED.getMaterials()[0], BossDropItem.KARIL_THE_BOBBLED.getMaterials()[1]),
				ItemPair.of(BossDropItem.TORAG_THE_BOBBLED.getMaterials()[0], BossDropItem.TORAG_THE_BOBBLED.getMaterials()[1]),
				ItemPair.of(BossDropItem.VERAC_THE_BOBBLED.getMaterials()[0], BossDropItem.VERAC_THE_BOBBLED.getMaterials()[1]),
		};
	}

}
