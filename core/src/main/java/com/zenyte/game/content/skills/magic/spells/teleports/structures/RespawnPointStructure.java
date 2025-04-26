package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 13. juuli 2018 : 22:12:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RespawnPointStructure extends ArceuusStructure {
	@Override
	public Location getRandomizedLocation(final Player player, final Teleport teleport) {
		final int randomization = teleport.getRandomizationDistance();
		final Location destination = player.getRespawnPoint().getLocation();
		if (randomization <= 0) {
			return destination;
		}
		int count = RANDOMIZATION_ATTEMPT_COUNT;
		while (--count > 0) {
			final Location tile = RandomLocation.random(destination, randomization);
			if (ProjectileUtils.isProjectileClipped(null, null, destination, tile, true)) continue;
			return tile;
		}
		return destination;
	}
}
