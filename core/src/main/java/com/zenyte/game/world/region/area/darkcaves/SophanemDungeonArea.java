package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:48:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SophanemDungeonArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(
                new int[][] { { 3198, 9280 }, { 3198, 9214 }, { 3330, 9214 }, { 3330, 9280 } }, 0) };
	}

	@Override
	public String name() {
		return "Sophanem dungeon";
	}

}
