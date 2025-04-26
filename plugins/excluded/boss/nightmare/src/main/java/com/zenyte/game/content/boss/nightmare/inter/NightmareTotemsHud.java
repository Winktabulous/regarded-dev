package com.zenyte.game.content.boss.nightmare.inter;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;

public class NightmareTotemsHud extends Interface {

	private static final Object[] FADE_IN_ARGS = {0, -1, 413 << 16 | 2, 413 << 16 | 3, 413 << 16 | 5, 413 << 16 | 7, 413 << 16 | 8, 413 << 16 | 9, 413 << 16 | 11, 413 << 16 | 13, 413 << 16 | 14, 413 << 16 | 15, 413 << 16 | 17, 413 << 16 | 19, 413 << 16 | 20, 413 << 16 | 21, 413 << 16 | 23, 413 << 16 | 25, 413 << 16 | 26, 413 << 16 | 27};

	@Override
	protected void attach() {

	}

	@Override
	protected void build() {

	}

	@Override
	protected void close(Player player) {
		player.getPacketDispatcher().sendClientScript(3323, FADE_IN_ARGS);
		WorldTasksManager.schedule(() -> super.close(player), 2);
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.NIGHTMARE_TOTEMS;
	}

}
