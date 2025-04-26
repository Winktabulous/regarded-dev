package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:34:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class MosLeHarmlessCavernArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(
				new int[][] { { 3711, 9469 }, { 3710, 9342 }, { 3749, 9339 }, { 3787, 9404 }, { 3844, 9404 }, { 3841, 9479 } }, 0) };
	}

	@Override
	public String name() {
		return "Mos Le'Harmless caves";
	}

}
