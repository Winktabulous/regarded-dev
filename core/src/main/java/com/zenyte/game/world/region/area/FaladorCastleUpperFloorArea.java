package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 20:27:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class FaladorCastleUpperFloorArea extends FaladorArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2958, 3334 }, { 2955, 3337 }, { 2955, 3338 }, { 2954, 3338 }, { 2954, 3340 }, { 2955, 3340 }, { 2955, 3341 }, { 2958, 3344 }, { 2963, 3344 }, { 2965, 3342 }, { 2965, 3336 }, { 2963, 3334 } }, 3) };
	}

	@Override
	public void enter(Player player) {
	}

	@Override
	public void leave(Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Falador Castle Upper Floor";
	}

}
