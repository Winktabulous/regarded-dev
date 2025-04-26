package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 28. aug 2018 : 00:04:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class KruksDungeonArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2306, 9289 }, { 2306, 9014 }, { 2686, 9014 }, { 2686, 9289 } }, 1) };
	}

	@Override
	public String name() {
		return "Kruk's dungeon";
	}

}
