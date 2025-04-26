package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.plugins.itemonitem.AbyssalTentacleItemCreationAction.ABYSSAL_TENTACLE;
import static com.zenyte.plugins.itemonitem.AbyssalTentacleItemCreationAction.KRAKEN_TENTACLE;

/**
 * @author Kris | 25. aug 2018 : 21:49:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public class AbyssalTentacleOr extends ItemPlugin {

	public static final Item ORN_KIT = new Item(26421);

	@Override
	public void handle() {
		bind("Dissolve", (player, item, slotId) -> {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You need some free inventory space to dissolve this item.");
				return;
			}

			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					plain("<col=FF0040>Warning!</col><br>Once the abyssal tentacle is dissolved, you only to get the kraken tentacle back making you lose the abyssal whip.");
					options("Are you sure you wish to do this?", "Yes, dissolve the abyssal tentacle", "No, I'll keep my abyssal tentacle.")
							.onOptionOne(() -> {
								if (!player.getInventory().hasFreeSlots()) {
									player.sendMessage("You need some free inventory space to dissolve this item.");
									return;
								}

								player.getInventory().deleteItem(item);
								player.getInventory().addItem(ORN_KIT);
								player.getInventory().addItem(KRAKEN_TENTACLE);
							});
				}
			});
		});
		bind("Dismantle kit", Dismantleable::dismantle);
	}

	@Override
	public int[] getItems() {
		return new int[] { 26484 };
	}

}
