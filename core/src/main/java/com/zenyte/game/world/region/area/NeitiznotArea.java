package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 22. sept 2018 : 22:15:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class NeitiznotArea extends FremennikIsles {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(
				new int[][] { { 2306, 3841 }, { 2382, 3841 }, { 2382, 3826 }, { 2368, 3826 }, { 2368, 3775 }, { 2306, 3774 } }) };
	}

	@Override
	public String name() {
		return "Neitiznot";
	}

}
