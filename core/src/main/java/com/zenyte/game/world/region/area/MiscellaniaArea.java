package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 22. sept 2018 : 22:23:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class MiscellaniaArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2491, 3925 }, { 2491, 3811 }, { 2627, 3812 }, { 2627, 3925 } }) };
	}

	@Override
	public void enter(final Player player) {

	}

	@Override
	public void leave(final Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Miscellania";
	}

}
