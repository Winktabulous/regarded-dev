package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.HousePortal;

import java.util.ArrayList;

/**
 * @author Tommeh | 16 nov. 2017 : 22:06:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class HousePortalObject implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (optionId == 1) {
			player.getDialogueManager().start(new HousePortal(player));
		} else if (optionId == 2) {
			player.getConstruction().enterHouse(false);
		} else if (optionId == 3) {
			player.getConstruction().enterHouse(true);
		} else {
			player.sendMessage("This feature hasn't been added yet.");
		}
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (int id = 15477; id < 15483; id++) {
			list.add(id);
		}
		list.add(28822);
		return list.toArray(new Object[list.size()]);
	}
}
