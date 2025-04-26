package com.zenyte.plugins.dialogue.lumbridge;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 22:42:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class DoomsayerD extends Dialogue {

	public DoomsayerD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Doooooom!");
		player("Where?");
		npc("All around us! I can feel it in the air, hear it on the wind, smell it... also in the air!");
		player("Is there anything we can do about this doom?");
		npc("There is nothing you need to do my friend! I am the Doomsayer, although my real title could be somthing like the Danger Tutor.");
		player("Danger Tutor?");
		npc("Yes! I roam the world sensing danger.");
		npc("If I find a dangerous area, then I put up warning signs to tell you whats is so dangerous about that area.");
		npc("If you see the signs often enough, then you can turn them off; by that time you likely know what the area has in store for you.");
		player("But what if I want to see the warnings again?");
		npc("That's why I'm waiting here!");
		npc("If you want to see the warning messages again, I can turn them back on for you.");
		npc("Do you need to turn on any warnings right now?");
		options(TITLE, new DialogueOption("Yes, I do.", () -> player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 583)),
				new DialogueOption("Not right now.", () -> setKey(30)));
		npc(30, "Ok, keep an eye out for the messages though!");
		player("I will.");
	}

}
