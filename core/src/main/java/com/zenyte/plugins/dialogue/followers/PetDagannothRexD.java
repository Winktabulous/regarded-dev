package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:46.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetDagannothRexD extends Dialogue {

	public PetDagannothRexD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Do you have any berserker rings?");
		npc("Nope.");
		player("You sure?");
		npc("Yes.");
		player("So, if I tipped you upside down and shook you, you'd not drop any berserker rings?");
		npc("Nope.");
		player("What if I endlessly killed your father for weeks on end, would I get one then?");
		npc("Been done by someone, nope.");
	}

}
