package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.player.Player;

public interface LogoutRestrictionPlugin {

    boolean manualLogout(final Player player);
}
