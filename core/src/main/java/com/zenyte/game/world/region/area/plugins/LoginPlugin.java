package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 29. juuni 2018 : 00:08:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface LoginPlugin {

	/**
	 * The method is referenced alongside the {@link #Area.enter(Player)} method.
	 * 
	 * @param player
	 *            the player who logged into the game while in this area.
	 */
    void login(final Player player);
	
}
