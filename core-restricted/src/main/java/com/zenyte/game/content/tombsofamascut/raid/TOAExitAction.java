package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Savions.
 */
public class TOAExitAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
		if (party == null) {
			player.setLocation(TOAManager.OUTSIDE_LOCATION);
			return;
		}
		player.getTOAManager().startLeaveDialogue();
	}

	@Override public Object[] getObjects() {
		return new Object[] {45128, 45453, 45543, 45144, 46055, 45844, 45129};
	}
}