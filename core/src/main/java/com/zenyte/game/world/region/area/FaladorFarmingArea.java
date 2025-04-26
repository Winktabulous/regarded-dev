package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 15:29:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class FaladorFarmingArea extends KingdomOfAsgarnia {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3044, 3315 }, { 3044, 3300 }, { 3062, 3300 }, { 3062, 3315 } }) };
	}

	@Override
	public String name() {
		return "Falador Farming";
	}

}
