package com.zenyte.game.world.entity.player.cutscene.actions;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. dets 2017 : 14:42.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class FadeScreenAction implements Runnable {

	public FadeScreenAction(final Player player, final int duration) {
		this(player, duration, null);
	}

	public FadeScreenAction(final Player player, final int duration, final Runnable runnable) {
		this.player = player;
		this.duration = duration;
		this.runnable = runnable;
	}

	private final Player player;
	private final int duration;
	private final Runnable runnable;

	@Override
	public void run() {	
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 174);
		player.getPacketDispatcher().sendClientScript(951);
		WorldTasksManager.schedule(() -> {
			player.getPacketDispatcher().sendClientScript(948, 0, 0, 0, 255, 50);
			if (runnable != null) {
				runnable.run();
			}
		}, duration);
	}

}
