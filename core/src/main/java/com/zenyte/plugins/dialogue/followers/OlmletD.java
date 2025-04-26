package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:30.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class OlmletD extends Dialogue {

	public OlmletD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Hee hee! What shall we talk about, human?");
		options(TITLE, 
				"Where do creatures like you come from?",
				"You look like a dragon.",
				"Can you tell me secrets about your home?",
				"Maybe another time.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10))
		.onOptionThree(() -> setKey(25)).onOptionFour(() -> setKey(35));
		player(5, "Where do creatures like you come from?");
		npc("From eggs, of course! You can't make an olmlet without breaking an egg.");
		player("That's... informative. Thank you.");
		npc("Hee hee! What's next, human?");
		options(TITLE, 
				"You look like a dragon.",
				"Can you tell me secrets about your home?",
				"Maybe another time.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(25))
		.onOptionThree(() -> setKey(35));
		player(10, "You look like a dragon.");
		npc("And humans look like monkeys. Badly shaved monkeys. What's your point, human?");
		player("Are you related to dragons?");
		npc("My sire was an olm. I'm an olm. I don't go around asking you about your parents' species, do I?");
		player("... no, I suppose you don't.");
		npc("Hee hee! Let's change the subject before someone gets insulted. What shall we talk about instead, human?");
		options(TITLE, 
				"Where do creatures like you come from?",
				"Can you tell me secrets about your home?",
				"Maybe another time.").onOptionOne(() -> setKey(5))
		.onOptionTwo(() -> setKey(25)).onOptionThree(() -> setKey(35));
		player(25, "Can you tell me secrets about your home?");
		npc("Ooh, it was lovely. I lived in an eggshell. I was safe in there, dreaming of the life I would lead when I hatched, "
				+ "and the caverns I could rule.");
		npc("Then suddenly I felt a trembling of the ground, and my shell shattered.");
		npc("Through its cracks I saw the world for the first time, just in time to watch my sire die.");
		npc("It was a terrible shock for a newly hatched olmlet, but I try not to let it affect my mood. "
				+ "So what else shall we talk about, human?");
		options(TITLE, 
				"Where do creatures like you come from?",
				"You look like a dragon.",
				"Maybe another time.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> setKey(35));
		player(35, "Maybe another time.");
	}

}
