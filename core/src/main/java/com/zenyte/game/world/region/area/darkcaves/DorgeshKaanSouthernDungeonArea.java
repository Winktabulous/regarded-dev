package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 27. aug 2018 : 23:16:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DorgeshKaanSouthernDungeonArea extends PolygonRegionArea {
    @Override
    public void enter(final Player player) {

    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2690, 5247 }, { 2690, 5184 }, { 2758, 5184 }, { 2758, 5247 } }) };
	}

	@Override
	public String name() {
		return "Dorgesh-Kaan Southern dungeon";
	}

}
