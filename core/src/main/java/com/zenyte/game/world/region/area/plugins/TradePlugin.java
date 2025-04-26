package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 1-12-2018 | 19:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface TradePlugin {

    boolean canTrade(final Player player, final Player partner);
}
