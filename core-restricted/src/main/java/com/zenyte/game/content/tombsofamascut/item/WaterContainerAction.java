package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Savions.
 */
public class WaterContainerAction extends ItemPlugin {

	@Override public void handle() {
		bind("Check", (player, item, container, slotId) -> player.sendMessage("Your water container is at " + item.getCharges() + "% capacity."));
		bind("Empty", (player, item, container, slotId) -> {
			if (item.getCharges() <= 0) {
				player.sendMessage("It's already empty.");
			} else {
				player.getDialogueManager().start(new OptionDialogue(player, "Empty water container", new String[] {"Yes, empty water container.", "No."}, new Runnable[]{() -> {
					item.setCharges(0);
					player.sendMessage("Your empty your water container.");
				}, null}));
			}
		});
	}

	@Override public int[] getItems() {
		return new int[] {CrondisPuzzleEncounter.CONTAINER_ITEM_ID};
	}
}
