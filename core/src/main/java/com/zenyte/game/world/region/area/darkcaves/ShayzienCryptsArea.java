package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:39:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ShayzienCryptsArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 1475, 9985 }, { 1475, 9923 }, { 1537, 9923 }, { 1537, 9985 } }, 1, 2, 3) };
	}

	@Override
	public String name() {
		return "Shayzien Crypts";
	}

}
