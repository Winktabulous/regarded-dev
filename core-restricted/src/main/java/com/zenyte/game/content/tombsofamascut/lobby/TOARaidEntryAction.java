package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Savions.
 */
public class TOARaidEntryAction implements ObjectAction {

	private static final Location OBELISK_LOCATION = new Location(3358, 9119, 0);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);

		if (currentLobbyParty == null) {
			final OptionDialogue dialogue = new OptionDialogue(player, "You are currently not in a raiding party.", new String[] {"Form or join a party.", "Cancel."},
					new Runnable[] {() -> ObjectHandler.handle(player, 46068, OBELISK_LOCATION, false, 1), null});
			player.getDialogueManager().start(dialogue);
		} else {
			player.getTOAManager().enterRaid();
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {46089};
	}
}
