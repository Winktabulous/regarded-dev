package com.zenyte.plugins.item;

import com.zenyte.game.content.treasuretrails.Puzzle;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 22:38:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PuzzleBoxItem extends ItemPlugin {
	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
			if (item.getCharges() == 1) {
				player.sendMessage("You've already completed this puzzle.");
				return;
			}
			player.getPuzzleBox().openPuzzle(item.getId());
		});
		bind("Check steps", (player, item, container, slotId) -> {
			final int steps = item.getNumericAttribute(TreasureTrail.Constants.CLUE_SCROLL_CURRENT_STEPS).intValue();
			player.sendMessage("You have completed " + steps + " step" + (steps == 1 ? "" : "s") + " on this " + item.getName().substring(12, item.getName().length() - 1) + " clue scroll.");
		});
	}

	@Override
	public int[] getItems() {
		return Puzzle.getPuzzleBoxArray();
	}
}
