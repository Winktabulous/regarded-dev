package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public class EnergyBarrierObject implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (option.equals("Escape")) {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Are you sure you want to escape?", new DialogueOption("Yes", () -> {
						player.setLocation(new Location(3569, 3358));
					}), new DialogueOption("No"));
				}
			});
		} else if (option.equals("Quick-escape")) {
			player.setLocation(new Location(3569, 3358));
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 38001 };
	}

}
