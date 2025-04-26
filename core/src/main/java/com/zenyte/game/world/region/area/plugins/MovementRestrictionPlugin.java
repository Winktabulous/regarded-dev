package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

public interface MovementRestrictionPlugin {

    boolean canMoveToLocation(final Player player, final Location destination);
}
