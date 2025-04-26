package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class TOAEncounterEntryAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
		if (party == null) {
			player.setLocation(TOAManager.OUTSIDE_LOCATION);
			return;
		}
		player.getTOAManager().advanceRaid(!"Enter".equals(option));
	}

	@Override public Object[] getObjects() {
		return new Object[] {45397, 45337, 45131, 45500};
	}
}
