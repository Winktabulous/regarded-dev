package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;

import static com.zenyte.game.GameInterface.SERVER_EVENTS;

public class ServerEventsInterface extends Interface {

	@Override
	protected void attach() {

	}

	@Override
	public void open(Player player) {
		player.getInterfaceHandler().sendInterface(getInterface().getId(), 33, PaneType.JOURNAL_TAB_HEADER, true);
		update(player);
	}

	public static void update(Player player) {
		StringBuilder sb = new StringBuilder();
		for (WorldBoost worldBoost : World.getWorldBoosts()) {
			long boostEnd = worldBoost.boostEnd();
			long hourTicks = TimeUnit.HOURS.toTicks(worldBoost.getDurationHours());
			long ticksleft = TimeUnit.MILLISECONDS.toTicks(boostEnd - System.currentTimeMillis());
			sb.append(worldBoost.getBoostType().getMssg());
			sb.append("|0|");
			sb.append(ticksleft);
			sb.append("|");
			sb.append(hourTicks);
			sb.append("|");
		}
		player.getPacketDispatcher().sendClientScript(10612, sb.toString());
	}
	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return SERVER_EVENTS;
	}

}
