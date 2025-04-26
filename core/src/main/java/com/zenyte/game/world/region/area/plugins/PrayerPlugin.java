package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 30-11-2018 | 19:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface PrayerPlugin {

    boolean activatePrayer(final Player player, final Prayer prayer);
}
