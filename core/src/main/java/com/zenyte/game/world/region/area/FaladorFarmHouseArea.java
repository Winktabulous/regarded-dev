package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 24 sep. 2018 | 20:06:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class FaladorFarmHouseArea extends KingdomOfAsgarnia {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3021, 3296 }, { 3021, 3282 }, { 3027, 3282 }, 
			{ 3028, 3281 }, { 3040, 3281 }, { 3042, 3283 }, { 3041, 3284 }, { 3041, 3297 }, 
			{ 3038, 3297 }, { 3038, 3298 }, { 3033, 3298 }, { 3033, 3297 }, { 3021, 3297 } }) };
	}

	@Override
	public String name() {
		return "Falador Farm House";
	}

}
