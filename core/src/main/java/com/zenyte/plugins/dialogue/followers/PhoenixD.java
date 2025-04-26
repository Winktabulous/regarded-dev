package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:03.59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PhoenixD extends Dialogue {

	public PhoenixD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(3);
		switch(option) {
		case 0:
			player("So... The Pyromancers, they're cool, right?");
			npc("We share a common goal..");
			player("Which is?");
			npc("Keeping the cinders burning and preventing the long night from swallowing us all.");
			player("That sounds scary.");
			npc("As long as we remain vigilant and praise the Sun, all will be well.");
			break;
		case 1:
			npc("...");
			player("What are you staring at?");
			npc("The great Sol Supra.");
			player("Is that me?");
			npc("No mortal. The Sun, as you would say.");
			player("Do you worship it?");
			npc("It is wonderous... If only I could be so grossly incandescent.");
			break;
		case 2:
			player("Who's a pretty birdy?");
			plain("The Phoenix Gives you a smouldering look.");
			break;
			default:
				npc("One day I will burn so hot I'll become Sacred Ash.");
				player("Aww, but you're so rare, where would I find another?");
				npc("Do not fret mortal, I will rise from the Sacred Ash greater than ever before.");
				player("So you're immortal?");
				npc("As long as the Sun in the sky gives me strength.");
				player("...Sky?");
				break;
		}
	}

}
