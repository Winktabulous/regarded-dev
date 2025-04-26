package com.zenyte.game.world.entity.player.cutscene;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. dets 2017 : 1:52.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CutsceneManager {
	private final Player player;
	private Cutscene cutscene;

	public CutsceneManager(final Player player) {
		this.player = player;
	}

	public void logout() {
		if (cutscene != null) cutscene.logout();
	}

	public void play(final Cutscene scene) {
		scene.setPlayer(player);
		scene.build();
		this.cutscene = scene;
	}

	public void finish() {
		this.cutscene = null;
	}

	public void process() {
		if (cutscene == null) return;
		final Cutscene cutscene = this.cutscene;
		if (cutscene.process()) return;
		if (this.cutscene == cutscene) {
			this.cutscene = null;
		}
	}
}
