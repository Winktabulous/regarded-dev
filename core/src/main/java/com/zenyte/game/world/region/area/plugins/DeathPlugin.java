package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 28. juuni 2018 : 22:48:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface DeathPlugin {

	boolean isSafe();

	default boolean isRingOfLifeEffective() {
	    return !isSafe();
    }

	String getDeathInformation();

	Location getRespawnLocation();

	default boolean sendDeath(final Player player, final Entity source) {
		return false;
	}

    default boolean loseHardcoreIronGroupLive() {
        return !isSafe();
    }

	default Location gravestoneLocation() {
		return null;
	}

}
