package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:13.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class SkotosD extends Dialogue {

	public SkotosD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		options(TITLE,
				"You look cute.",
				"Where did you come from?",
				"What can you do for me?")
		.onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(15)).onOptionThree(() -> setKey(30));
		player(5, "You look cute.");
		npc("I do not thinke thou understand the depths of the darkness you have unleased upon the world. "
				+ "To dub it in such a scintillant manner is offensive to mine being.");
		player("So why are you following me around.");
		npc("Dark forces of which ye know nought have deemed that this is my geas.");
		player("Your goose?");
		npc("*Sighs* Nae. But thine is well and truly cooked.");
		player(15, "Where did you come from?");
		npc("I am spawned of darkness. I am filled with darkness. I am darkness incarnate and to darkness I will return.");
		player("Sounds pretty... dark.");
		npc("Knowest thou not of the cursed place? Knowest thou not about the future yet to befall your puny race?");
		player("Oh yes, I've heard that before.");
		npc("Then it is good that ye can laugh in the face of the end.");
		player("The end has a face? Which end?");
		npc("*Sighs* The darkness giveth, and the darkness taketh.");
		player(30, "What can you do for me?");
		npc("Nothing. Ye are already tainted in my sight by the acts of light. However they may be some hope for you "
				+ "if you continue to aid the darkness.");
		player("I do have a lantern around here somewhere.");
		npc("Do not bring that foul and repellant thing near mine self.");
	}

}
