package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import static com.zenyte.utils.TimeUnit.MILLISECONDS;

/**
 * @author Kris | 15. juuli 2018 : 22:32:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class HouseStructure extends RegularStructure {
	@Override
	public boolean isTeleportPrevented(final Player player, final Teleport teleport) {
		if (!player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
			player.sendMessage("Construction is incomplete, therefore disabled.");
			return true;
		}
		if (player.getConstruction().getHouse() == null) {
			player.sendMessage("You need to purchase yourself a house before you may use this teleport.");
			return true;
		}
		return false;
	}

	@Override
	public void end(final Player player, final Teleport teleport) {
		if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isRestricted(player, teleport)) {
			stop(player, teleport);
			return;
		}
		final Animation endAnimation = Utils.getOrDefault(getEndAnimation(), Animation.STOP);
		final Graphics endGraphics = Utils.getOrDefault(getEndGraphics(), Graphics.RESET);
		final Location location = getRandomizedLocation(player, teleport);
		verifyLocation(player, location);
		player.setLocation(location);
		teleport.onArrival(player);
		player.setAnimation(endAnimation);
		player.setGraphics(endGraphics);
		player.getInterfaceHandler().closeInterfaces();
		WorldTasksManager.scheduleOrExecute(() -> stop(player, teleport), (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(endAnimation)) - 1);
		updateDiaries(player, teleport);
	}

	@Override
	public void stop(final Player player, final Teleport teleport) {
		super.stop(player, teleport);
		player.getConstruction().enterHouse(false);
	}

	@Override
	public Location getRandomizedLocation(final Player player, final Teleport teleport) {
		return player.getLocation();
	}
}
