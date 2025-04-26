package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 6. veebr 2018 : 21:01.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VenomCloud extends WorldObject {

	public VenomCloud(final Location tile) {
		super(11700, 10, 0, new Location(tile.getX() - 1, tile.getY() - 1, tile.getPlane()));
	}

}
