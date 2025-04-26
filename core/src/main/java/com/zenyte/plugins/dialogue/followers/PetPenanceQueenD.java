package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:55.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetPenanceQueenD extends Dialogue {

	public PetPenanceQueenD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Of all the high gamble rewards I could have won, I won you...");
		npc("Keep trying, human. You'll never win that Dragon Chainbody.");
	}

}
