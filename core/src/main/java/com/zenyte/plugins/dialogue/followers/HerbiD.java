package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:14.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class HerbiD extends Dialogue {

	public HerbiD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(4);
		switch(option) {
		case 0:
			player("Are you hungry?");
			npc("That depends, what have you got?");
			player("I'm sure I could knock you up a decent salad.");
			npc("I'm actually an insectivore.");
			player("Oh, but your name suggests that-");
			npc("I think you'll find I didn't name myself, you humans and your silly puns.");
			player("No need to PUNish us for our incredible wit.");
			npc("Please. Stop.");
			break;
		case 1:
			player("Have your herbs died?");
			npc("These old things? I guess they've dried up... "
					+ "I'm getting old and I need caring for. I've chosen you to do that by the way.");
			player("Oh fantastic! I guess I'll go shell out half a million coins to keep "
					+ "you safe then, what superb luck!");
			npc("I could try the next person if you'd prefer?");
			player("I'm just joking you old swine!");
			break;
		case 2:
			player("So you live in a hole? I would've thought Boars are surface dwelling mammals.");
			npc("Well, I'm special! I bore down a little so I'm nice and cosy with my herbs exposed "
					+ "to the sun, it's all very interesting.");
			player("Sounds rather... Boring!");
			npc("How very original...");
			break;
		case 3:
			player("Tell me... do you like Avacado?");
			npc("I'm an insectivore, but even if I wasn't I'd hate Avacado!");
			player("Why ever not? It's delicious!");
			npc("I don't know why people like it so much... it tastes a like a ball of chewed up grass.");
			player("Sometimes you can be such a bore...");
			break;
			default:
				npc("When I was a young HERBIBOAR!!");
				player("I'm standing right next to you, no need to shout...");
				npc("I was trying to sing you a song...");
				break;
		}
	}

}
