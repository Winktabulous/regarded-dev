package com.zenyte.plugins.flooritem;

import com.zenyte.game.content.skills.firemaking.Firemaking;
import com.zenyte.game.content.skills.firemaking.FiremakingAction;
import com.zenyte.game.content.skills.firemaking.FiremakingTool;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 27. march 2018 : 22:03.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LogsFloorPlugin implements FloorItemPlugin {
	@Override
	public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
		if (item.getLocation().getPositionHash() != player.getLocation().getPositionHash()) {
			player.sendMessage("You can't light the logs from here.");
			return;
		}
		final Firemaking firemakingType = Firemaking.MAP.get(item.getId());
		if (firemakingType == null) {
			return;
		}
		final Optional<FiremakingTool> tool = FiremakingTool.getAvailableTool(player);
		if (!tool.isPresent()) {
			player.sendMessage("You cannot light a fire without the proper tool.");
			return;
		}
		player.getActionManager().setAction(new FiremakingAction(firemakingType, -1, true, tool.get(), Optional.of(item)));
	}

	@Override
	public int[] getItems() {
		final List<Integer> ids = new ArrayList<Integer>();
		for (final Firemaking data : Firemaking.VALUES) {
			ids.add(data.getLogs().getId());
		}
		return ids.stream().mapToInt(i -> i).toArray();
	}
}
