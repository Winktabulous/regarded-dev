package com.zenyte.game.world.region;

import com.zenyte.game.world.entity.player.Player;

public interface PlayerPredicate {

	boolean test(final Player player);
	
}
