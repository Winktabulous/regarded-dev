package com.zenyte.plugins.item;

import com.zenyte.game.content.rots.RotsManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class BarrowsTotem extends ItemPlugin {

	@Override
	public void handle() {
		bind("Activate", (player, item, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(new Item(32168), "Totem starts glowing, activating it will teleport you somewhere.");
					options(new DialogueOption("Consume the totem.", () -> {
						if (!player.getInventory().deleteItem(new Item(32168)).isFailure()) {
							RotsManager.createInstance(player);
						}
					}), new DialogueOption("Leave."));
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 32168 };
	}

}
