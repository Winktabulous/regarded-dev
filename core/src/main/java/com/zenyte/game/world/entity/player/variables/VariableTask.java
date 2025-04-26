package com.zenyte.game.world.entity.player.variables;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 5. juuni 2018 : 02:43:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface VariableTask {

	void run(final Player player, final int ticks);
	
}
