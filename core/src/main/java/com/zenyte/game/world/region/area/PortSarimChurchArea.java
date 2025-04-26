package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 19:52:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class PortSarimChurchArea extends KingdomOfAsgarnia {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2994, 3181 }, { 2994, 3175 }, { 3000, 3175 }, { 3000, 3176 }, { 3001, 3176 }, { 3001, 3180 }, { 3000, 3180 }, { 3000, 3181 } }) };
	}

	@Override
	public String name() {
		return "Port Sarim Church";
	}

}
