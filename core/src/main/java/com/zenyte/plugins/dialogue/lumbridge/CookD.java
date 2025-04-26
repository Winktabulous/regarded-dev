package com.zenyte.plugins.dialogue.lumbridge;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 22:42:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class CookD extends Dialogue {

	public CookD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Welcome to my kitchen adventurer. Feel free to use any cooking equipment you need. Make sure you wash up though; can't be having dirty pie dishes all over the place!");
	}

}
