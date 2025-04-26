package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25. aug 2018 : 22:50:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ZulrahScaledItem extends ItemPlugin {
	@Override
	public void handle() {
		bind("Dismantle", (player, item, slotId) -> {
			final String name = item.getName().replace(" (uncharged)", "");
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(item, "Dismantling the " + name + " is an irreversible process which grants you 20,000 " +
                            "Zulrah's scales.");
					options("Dismantle the " + name + "?", new DialogueOption("Dismantle it.", () -> {
						player.getInventory().ifDeleteItem(item, () -> {
							player.getInventory().addItem(12934, 20000);
							player.sendMessage("You dismantle the " + name.toLowerCase() + ".");
						});
					}), new DialogueOption("Keep it."));
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {12922, 12927, 12929, 12932};
	}
}
