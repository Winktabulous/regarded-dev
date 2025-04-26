package com.zenyte.game.world.entity.player.cutscene.actions;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. dets 2017 : 1:50.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CameraResetAction implements Runnable {

	public CameraResetAction(final Player player) {
		this.player = player;
	}
	
	private final Player player;
	
	@Override
	public void run() {
		player.getPacketDispatcher().resetCamera();
	}

}
