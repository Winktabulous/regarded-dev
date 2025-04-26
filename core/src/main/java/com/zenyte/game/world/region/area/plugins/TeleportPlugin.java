package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 9. juuli 2018 : 01:11:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface TeleportPlugin {

	boolean canTeleport(final Player player, final Teleport teleport);
	
}
