package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:59.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetZilyanaD extends Dialogue {

	public PetZilyanaD(final Player player, final NPC npc) {
		super(player, npc);
	}
	
	private static final Item GODSWORD = new Item(11806), NOTED_GODSWORD = new Item(11807), ORNAMENT_GODSWORD = new Item(20372);

	@Override
	public void buildDialogue() {
		final boolean inInventory = player.getInventory().containsItem(GODSWORD) || player.getInventory().containsItem(NOTED_GODSWORD) || player.getInventory().containsItem(ORNAMENT_GODSWORD);
		final boolean inBank = player.getBank().containsItem(GODSWORD) || player.getBank().containsItem(NOTED_GODSWORD) || player.getBank().containsItem(ORNAMENT_GODSWORD);
		if (inInventory || inBank) {
			player("I FOUND THE GODSWORD!");
			npc("GOOD!!!!!");
		} else {
			player("FIND THE GODSWORD!");
			npc("FIND THE GODSWORD!");
		}
	}

}
