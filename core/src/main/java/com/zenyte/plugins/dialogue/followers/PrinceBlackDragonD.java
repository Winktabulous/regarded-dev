package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:07.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PrinceBlackDragonD extends Dialogue {

	public PrinceBlackDragonD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Shouldn't a prince only have two heads?");
		npc("Why is that?");
		player("Well, a standard Black dragon has one, the King has three so inbetween must have two?");
		npc("You're overthinking this.");
	}

}
