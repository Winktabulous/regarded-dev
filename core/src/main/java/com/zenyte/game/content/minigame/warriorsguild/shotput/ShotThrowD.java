package com.zenyte.game.content.minigame.warriorsguild.shotput;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 23:13.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotThrowD extends Dialogue {

	public ShotThrowD(final Player player, final int npcId, final int yards) {
		super(player, npcId);
		this.yards = yards;
	}
	
	private final int yards;

	@Override
	public void buildDialogue() {
		npc("Well done. You threw the shot " + yards + " yard" + (yards == 1 ? "" : "s") + "!");
	}

}
