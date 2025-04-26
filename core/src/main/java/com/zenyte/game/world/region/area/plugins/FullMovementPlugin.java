package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 1. juuli 2018 : 17:59:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface FullMovementPlugin extends PartialMovementPlugin {

	@Override
    boolean processMovement(final Player player, final int x, final int y);
	
}
