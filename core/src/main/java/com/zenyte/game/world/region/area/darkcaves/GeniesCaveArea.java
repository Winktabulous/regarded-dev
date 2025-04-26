package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:04:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class GeniesCaveArea extends DarkArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {
				new RSPolygon(new int[][] { { 3339, 9595 }, { 3327, 9573 }, { 3345, 9527 }, { 3383, 9537 }, { 3380, 9597 } }, 0) };
	}

	@Override
	public String name() {
		return "Genie's cave";
	}

}
