package com.zenyte.plugins.dialogue.lumbridge;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 21:56:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class ArcherD extends Dialogue {

	public ArcherD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("I'm wearing a green dragonhide body - and I haven't even done dragon slayer. Speak to me again for the secret.");
	}

}
