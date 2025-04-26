package com.zenyte.game.world;

import com.zenyte.game.task.WorldTasksManager;

/**
 * @author Kris | 4. juuni 2018 : 18:57:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ProjectileResult {

	private Runnable runnable;

	public void schedule(final Runnable runnable) {
		this.runnable = runnable;
	}

	void execute(final int delay) {
		WorldTasksManager.schedule(() -> {
			if (runnable == null) {
				return;
			}
			runnable.run();
		}, delay);
	}

}
