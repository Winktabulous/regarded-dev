package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Glabay | Glabay-Studios
 * @project Near-Reality
 * @social Discord: Glabay
 * @since 2024-08-27
 */
public class DragonHunterCrossbow extends ItemPlugin {

	@Override
	public void handle() {
		bind("Dismantle", this::dismantleDragonHunterCrossbow);
	}

	private void dismantleDragonHunterCrossbow(Player player, Item item, int slotId) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("<col=FF0040>Warning!</col><br>You are about to restore your crossbow,<br>you will not get the head back.");
				options("Are you sure you wish to do this?",
					"Yes, revert my crossbow.",
					"No, I'll keep my crossbow.")
					.onOptionOne(() -> {
						if(player.getInventory().deleteItem(item.getId(), 1).getSucceededAmount() == 1) {
							player.getInventory().addItem(ItemId.DRAGON_HUNTER_CROSSBOW, 1);
						}
					});
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {
			ItemId.DRAGON_HUNTER_CROSSBOW_B,
			ItemId.DRAGON_HUNTER_CROSSBOW_T
		};
	}

}
