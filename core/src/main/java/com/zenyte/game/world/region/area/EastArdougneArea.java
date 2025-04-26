package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 24. sept 2018 : 03:29:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class EastArdougneArea extends ArdougneArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] {{ 2559, 3258 },
				{ 2559, 3375 },
				{ 2689, 3375 },
				{ 2690, 3259 } }) };
	}

	@Override
	public String name() {
		return "East Ardougne";
	}

}
