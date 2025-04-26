package com.zenyte.game.world;

import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 14. juuli 2018 : 00:17:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface Position {

	Location getPosition();
	
	default boolean matches(final Position other) {
		if (other == null) {
			return false;
		}
		return other.getPosition().getPositionHash() == getPosition().getPositionHash();
	}
	
}
