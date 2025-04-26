package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:18.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class TzRekJadD extends Dialogue {

	public TzRekJadD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(1);
		if (option == 1) {
			player("Do you miss your people?");
			npc("Mej-TzTok-Jad Kot-Kl!");
			player("No.. I don't think so.");
			npc("Jal-Zek Kl?");
			player("No, no, I wouldn't hurt you.");
		} else {
			player("Are you hungry?");
			npc("Kl-Kra!");
			player("Ooookay...");
		}
	}

}
