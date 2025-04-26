package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 18. juuli 2018 : 01:18:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RingOfReturningStructure implements TeleportStructure {
	private static final Animation START_ANIM = new Animation(714);
	private static final Graphics START_GFX = new Graphics(111, 0, 70);

	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GFX;
	}

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
			if (ProjectileUtils.isProjectileClipped(player, null, destination, tile, true)) continue;
			return tile;
		}
		return destination;
	}
}
