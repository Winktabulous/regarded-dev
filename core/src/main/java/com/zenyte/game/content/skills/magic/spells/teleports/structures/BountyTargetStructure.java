package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15. juuli 2018 : 18:59:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BountyTargetStructure extends RegularStructure {
	@Override
	public boolean isTeleportPrevented(final Player player, final Teleport teleport) {
		final Object target = player.getTemporaryAttributes().get("bountyHunterTarget");
		if (!(target instanceof Player) || ((Player) target).isFinished() || player.getVarManager().getBitValue(2010) == 0) {
			player.sendMessage("You do not have a bounty hunter target.");
			return true;
		} else if (player.getVarManager().getBitValue(5604) >= 5) {
			player.sendMessage("You've reached your quota of teleportations to this target.");
			return true;
		}
		return false;
	}

	@Override
	public Location getRandomizedLocation(final Player player, final Teleport teleport) {
		final Object target = player.getTemporaryAttributes().get("bountyHunterTarget");
		if (!(target instanceof Player) || ((Player) target).isFinished()) {
			return new Location(player.getLocation());
		}
		final int randomization = teleport.getRandomizationDistance();
		final Location destination = ((Player) target).getLocation();
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
