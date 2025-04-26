package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:43.08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetChaosElementalD extends Dialogue {

	public PetChaosElementalD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Is it true a level 3 skiller caught one of your siblings?");
		npc("Yes, they killed my mummy, kidnapped my brother, smiled about it and went to sleep.");
		player("Aww, well you have me now! I shall call you Squishy and you shall be mine and you shall be my Squishy.");
		player("Come on, Squishy come on, little Squishy!");
	}

}
