package com.zenyte.plugins.dialogue.magebank;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 29 mei 2018 | 19:12:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChamberGuardianD extends Dialogue{

	public ChamberGuardianD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Hi.");
		npc("Hello adventurer, would you like to buy one of the three powerful staves?");
		options(TITLE, "Yes.", "No, thank you.")
			.onOptionOne(() -> player.openShop("Mage Arena Staffs"));
	}

}
