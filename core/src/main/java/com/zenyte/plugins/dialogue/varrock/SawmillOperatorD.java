package com.zenyte.plugins.dialogue.varrock;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 28 mei 2018 | 20:50:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SawmillOperatorD extends Dialogue {

	public SawmillOperatorD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Do you want me to make some planks for you? Or<br>would you be interested in some other housing supplies?");
		options(TITLE, "Planks please!", "What kind of planks can you make?", "Can I buy some housing supplies?", "Nothing, thanks")
			.onOptionOne(() -> setKey(5))
		    .onOptionTwo(() -> setKey(10))
		    .onOptionThree(() -> player.openShop("Construction supplies"))
		    .onOptionFour(() -> setKey(15));
		player(5, "Planks please!");
		npc("What kind of planks do you want?").executeAction(() -> player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 403));
		player(10, "What kind of planks can you make?");
		npc("I can make planks from wood, oak, teak and mahogany.<br>I don't make planks from other woods as they're no<br>good for making furniture.");
		npc("Wood and oak are all over the place, but teak and mahogany can only be found in a few places like<br>Karamja and Etceteria.");
		player(15, "Nothing, thanks.");
		npc("Well come back when you want some. You can't get<br>good quality planks anywhere but here!");
	}

}
