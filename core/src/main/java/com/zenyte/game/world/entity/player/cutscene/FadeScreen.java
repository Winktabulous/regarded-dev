package com.zenyte.game.world.entity.player.cutscene;

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
public final class FadeScreen {

	public FadeScreen(final Player player) {
		this(player, null);
	}

	public FadeScreen(final Player player, final Runnable runnable) {
		this.player = player;
		this.runnable = runnable;
	}

	private final Player player;
	private final Runnable runnable;

	public void fade() {
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 174);
		player.getPacketDispatcher().sendClientScript(951);
		player.lock();
	}

    public void fade(final int ticks) {
        fade(ticks, true);
    }

	public void fade(final int ticks, final boolean unlock) {
		fade();
		WorldTasksManager.schedule(() -> unfade(unlock), ticks);
	}

	public void fade(final int ticks, final boolean unlock, Runnable runnable) {
		fade();
		WorldTasksManager.schedule(() -> {
			unfade(unlock);
			runnable.run();
		}, ticks);
	}
	public void unfade() {
		unfade(true);
	}

	public void unfade(final boolean unlock) {
		unfade(unlock, null);
	}

	public void unfade( boolean unlock, Runnable task) {
		player.getPacketDispatcher().sendClientScript(948, 0, 0, 0, 255, 50);
		if (runnable != null) {
			runnable.run();
		}
		if (unlock) {
			WorldTasksManager.schedule(player::unlock);
		}
		if (task != null) {
			WorldTasksManager.schedule(task::run, 2);
		}
	}

	public void unfade(boolean unlock, Runnable task, int delay) {
		player.getPacketDispatcher().sendClientScript(948, 0, 0, 0, 255, 50);
		if (runnable != null) {
			runnable.run();
		}
		if (unlock) {
			WorldTasksManager.schedule(player::unlock);
		}
		if (task != null) {
			WorldTasksManager.schedule(task::run, delay);
		}
	}

}
