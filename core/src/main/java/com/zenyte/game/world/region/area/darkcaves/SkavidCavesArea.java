package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:43:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SkavidCavesArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2495, 9472 }, { 2495, 9403 }, { 2562, 9403 }, { 2562, 9472 } }, 0) };
	}

	@Override
	public String name() {
		return "Skavid Caves";
	}

}
