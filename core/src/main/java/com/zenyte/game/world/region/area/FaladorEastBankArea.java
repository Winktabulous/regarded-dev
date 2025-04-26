package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 19:24:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class FaladorEastBankArea extends FaladorArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3009, 3359 }, { 3009, 3353 }, { 3022, 3353 }, { 3022, 3357 }, { 3019, 3357 }, { 3019, 3359 } }) };
	}

	@Override
	public void enter(Player player) {
	}

	@Override
	public void leave(Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Falador East Bank";
	}

}
