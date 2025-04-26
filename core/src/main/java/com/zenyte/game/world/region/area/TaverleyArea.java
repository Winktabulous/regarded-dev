package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 17:02:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class TaverleyArea extends KingdomOfKandarin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2937, 3492 }, { 2896, 3492 }, { 2891, 3484 }, { 2873, 3485 }, { 2872, 3475 }, { 2878, 3461 }, { 2877, 3444 }, { 2867, 3431 }, { 2867, 3419 }, { 2871, 3413 }, { 2877, 3402 }, { 2878, 3392 }, { 2880, 3392 },
                { 2892, 3378 }, { 2936, 3389 }, { 2941, 3393 }, { 2938, 3395 }, { 2944, 3415 }, { 2944, 3444 }, { 2940, 3448 }, { 2938, 3448 }, { 2936, 3449 }, { 2936, 3454 }, { 2937, 3455 }, { 2939, 3455 }, { 2940, 3456 }, { 2940, 3472 }, { 2937, 3475 } }) };
	}

	@Override
	public String name() {
		return "Taverley";
	}

}
