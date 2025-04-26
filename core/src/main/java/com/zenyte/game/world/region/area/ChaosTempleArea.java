package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 24 sep. 2018 | 23:06:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class ChaosTempleArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2930, 3517 }, { 2930, 3513 }, { 2935, 3513 }, { 2937, 3514 }, { 2937, 3515 }, { 2938, 3516 }, { 2940, 3516 },
			{ 2941, 3517 }, { 2941, 3518 }, { 2940, 3519 }, { 2939, 3519 }, { 2938, 3518 }, { 2936, 3518 }, { 2935, 3519 }, { 2933, 3519 }, { 2932, 3518 }, { 2931, 3518 } }) };
	}

	@Override
	public void enter(Player player) {
	}

	@Override
	public void leave(Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Chaos Temple";
	}

}
