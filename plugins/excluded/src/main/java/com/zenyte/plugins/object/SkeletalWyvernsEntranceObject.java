package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 2 jun. 2018 | 21:39:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SkeletalWyvernsEntranceObject implements ObjectAction {
	
	private static final Location INSIDE_LOCATION = new Location(3056, 9555, 0);
	private static final Location OUTSIDE_LOCATION = new Location(3056, 9562, 0);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (object.getId() == 10595) {
			player.setLocation(OUTSIDE_LOCATION);
		} else {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					plain("STOP! The creatures in this cave are VERY dangerous.<br><br>Are you sure you want to enter?");
					options(TITLE, "Enter.", "Don't enter.")
						.onOptionOne(() -> player.setLocation(INSIDE_LOCATION));
				}
			});
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 10595, 10596,  };
	}

}
