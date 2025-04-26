package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:54.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetKreeArraD extends Dialogue {

	public PetKreeArraD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Huh... that's odd... I thought that would be big news.");
		npc("You thought what would be big news?");
		player("Well there seems to be an absence of a certain ornithological piece: "
				+ "a headline regarding mass awareness of a certain avian variety.");
		npc("What are you talking about?");
		player("Oh have you not heard? It was my understanding that everyone had heard....");
		npc("Heard wha...... OH NO!!!!?!?!!?!");
		player("OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD. OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD.");
		plain("There's a slight pause as Kree'Arra Jr. goes stiff.");
	}

}
