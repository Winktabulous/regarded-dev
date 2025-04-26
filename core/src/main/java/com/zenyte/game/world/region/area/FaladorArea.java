package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 16. mai 2018 : 16:14:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FaladorArea extends KingdomOfAsgarnia {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2937, 3321 }, { 2941, 3321 }, { 2941, 3314 }, { 2943, 3312 }, { 2957, 3312 },
				{ 2958, 3311 }, { 2967, 3311 }, { 2968, 3310 }, { 2984, 3310 }, { 2991, 3317 }, { 2994, 3317 }, { 3001, 3324 },
				{ 3005, 3324 }, { 3005, 3323 }, { 3012, 3323 }, { 3016, 3327 }, { 3023, 3327 }, { 3025, 3329 }, { 3058, 3329 },
				{ 3059, 3328 }, { 3060, 3328 }, { 3061, 3329 }, { 3061, 3330 }, { 3060, 3331 }, { 3060, 3363 }, { 3066, 3369 },
				{ 3066, 3385 }, { 3061, 3390 }, { 3048, 3390 }, { 3047, 3389 }, { 3041, 3389 }, { 3040, 3390 }, { 3022, 3390 },
				{ 3020, 3392 }, { 3011, 3392 }, { 3008, 3395 }, { 3003, 3395 }, { 3002, 3394 }, { 2997, 3394 }, { 2996, 3393 },
				{ 2987, 3393 }, { 2985, 3395 }, { 2969, 3395 }, { 2963, 3395 }, { 2963, 3394 }, { 2958, 3394 }, { 2957, 3393 },
				{ 2949, 3393 }, { 2948, 3394 }, { 2946, 3394 }, { 2946, 3393 }, { 2945, 3392 }, { 2943, 3392 }, { 2937, 3386 } }) };
	}

	@Override
	public String name() {
		return "Falador";
	}

}
