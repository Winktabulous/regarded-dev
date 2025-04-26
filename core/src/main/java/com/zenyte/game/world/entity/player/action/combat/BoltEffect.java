package com.zenyte.game.world.entity.player.action.combat;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.Player;

public interface BoltEffect {

	double applyBoltEffect(final Player player, final Entity target, final Hit hit, boolean apply);
	
}
