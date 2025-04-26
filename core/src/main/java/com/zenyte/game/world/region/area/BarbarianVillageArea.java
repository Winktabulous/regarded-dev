package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 16. mai 2018 : 16:28:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BarbarianVillageArea extends KingdomOfMisthalin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3072, 3420 }, { 3072, 3413 }, { 3080, 3405 }, { 3089, 3405 }, { 3091, 3407 },
				{ 3091, 3412 }, { 3089, 3414 }, { 3089, 3416 }, { 3091, 3418 }, { 3092, 3418 }, { 3093, 3417 }, { 3095, 3417 },
				{ 3097, 3419 }, { 3098, 3419 }, { 3098, 3422 }, { 3095, 3422 }, { 3094, 3423 }, { 3091, 3423 }, { 3088, 3426 },
				{ 3088, 3435 }, { 3086, 3437 }, { 3086, 3446 }, { 3083, 3449 }, { 3074, 3449 }, { 3073, 3448 }, { 3073, 3435 },
				{ 3072, 3434 }, { 3072, 3425 }, { 3073, 3424 }, { 3073, 3421 } }) };
	}

	@Override
	public String name() {
		return "Barbarian Village";
	}

}
