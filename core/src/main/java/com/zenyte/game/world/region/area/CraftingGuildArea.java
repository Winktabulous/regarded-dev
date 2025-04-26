package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 29. mai 2018 : 17:27:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CraftingGuildArea extends KingdomOfAsgarnia {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(
				new int[][] { { 2928, 3292 }, { 2928, 3287 }, { 2930, 3285 }, { 2930, 3281 }, { 2927, 3278 }, { 2927, 3276 },
						{ 2928, 3275 }, { 2929, 3275 }, { 2933, 3279 }, { 2937, 3279 }, { 2937, 3278 }, { 2939, 3276 }, { 2943, 3276 },
						{ 2944, 3277 }, { 2944, 3291 }, { 2943, 3292 }, { 2939, 3292 }, { 2938, 3293 }, { 2936, 3293 }, { 2935, 3292 },
						{ 2935, 3290 }, { 2934, 3289 }, { 2933, 3289 }, { 2932, 3290 }, { 2932, 3292 }, { 2931, 3293 }, { 2929, 3293 } },
				0, 1) };
	}

	@Override
	public String name() {
		return "Crafting guild";
	}

}
