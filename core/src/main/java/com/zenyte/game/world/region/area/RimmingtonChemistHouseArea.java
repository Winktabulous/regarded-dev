package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 24 sep. 2018 | 22:36:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class RimmingtonChemistHouseArea extends Rimmington {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2925, 3211 }, { 2925, 3207 }, { 2939, 3207 }, { 2940, 3208 }, 
			{ 2940, 3210 }, { 2939, 3211 }, { 2937, 3211 }, { 2937, 3214 }, { 2929, 3214 }, { 2929, 3211 } }) };
	}

	@Override
	public String name() {
		return "Rimmington Chemist House";
	}

}
