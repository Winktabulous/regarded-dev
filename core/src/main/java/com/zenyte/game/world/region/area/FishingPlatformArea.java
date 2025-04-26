package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 16. aug 2018 : 18:28:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FishingPlatformArea extends FishingGuildArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2607, 3447 }, { 2607, 3441 }, { 2614, 3441 }, { 2614, 3440 }, { 2616, 3440 },
				{ 2616, 3441 }, { 2623, 3441 }, { 2623, 3447 } }, 0) };
	}

	@Override
	public String name() {
		return "Fishing platform";
	}

}
