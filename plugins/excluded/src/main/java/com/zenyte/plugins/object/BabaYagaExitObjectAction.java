package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 17 jul. 2018 | 17:29:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BabaYagaExitObjectAction implements ObjectAction {
	
	private static final Location LOCATION = new Location(2094, 3930, 0);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.setLocation(LOCATION);
	}
	
	@Override
	public Object[] getObjects() {
		return new Object[] { 16774 };
	}

}
