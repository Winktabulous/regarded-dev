package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 30/11/2018 15:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface TeleportMovementPlugin {

    void processMovement(final Player player, final Location destination);
}
