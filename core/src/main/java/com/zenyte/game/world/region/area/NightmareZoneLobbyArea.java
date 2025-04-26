package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 29. mai 2018 : 01:15:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class NightmareZoneLobbyArea extends KingdomOfKandarin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2600, 3120 }, { 2600, 3112 }, { 2608, 3112 }, { 2608, 3120 } }, 0) };
	}


	@Override
	public String name() {
		return "Nightmare Zone Lobby";
	}

}
