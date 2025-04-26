package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 29. mai 2018 : 05:20:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CookingGuildArea extends KingdomOfMisthalin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3138, 3452 }, { 3138, 3448 }, { 3142, 3444 }, { 3145, 3444 }, { 3149, 3448 },
				{ 3149, 3452 }, { 3147, 3454 }, { 3140, 3454 } }, 0, 1, 2) };
	}

	@Override
	public void enter(final Player player) {

	}

	@Override
	public void leave(final Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Cook's Guild";
	}

}
