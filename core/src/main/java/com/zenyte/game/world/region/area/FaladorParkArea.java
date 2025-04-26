package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 24 sep. 2018 | 19:47:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class FaladorParkArea extends FaladorArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2981, 3382 }, { 2981, 3389 }, { 2984, 3392 }, { 3002, 3392 }, 
			{ 3009, 3392 }, { 3010, 3391 }, { 3015, 3391 }, { 3017, 3389 }, { 3022, 3389 }, { 3027, 3384 }, { 3026, 3383 }, 
			{ 3026, 3381 }, { 3027, 3380 }, { 3027, 3378 }, { 3026, 3376 }, { 3026, 3375 }, { 3026, 3374 }, { 3024, 3373 }, 
			{ 3025, 3371 }, { 3022, 3368 }, { 3019, 3368 }, { 3019, 3367 }, { 3014, 3367 }, { 3012, 3369 }, { 3002, 3369 }, 
			{ 2998, 3373 }, { 2994, 3373 }, { 2991, 3376 }, { 2990, 3376 }, { 2986, 3380 }, { 2983, 3380 } }) };
	}

	@Override
	public void enter(final Player player) {

	}

	@Override
	public void leave(final Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Falador Park";
	}

}
