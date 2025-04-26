package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.smithing.CannonballSmithing;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 10 jun. 2018 | 16:21:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CannonballSmithingD extends SkillDialogue {

	public CannonballSmithingD(Player player) {
		super(player, "How many bars would you like to smith?", CannonballSmithing.PRODUCT);
	}

	@Override
	public void run(final int slotId, final int amount) {
		player.getActionManager().setAction(new CannonballSmithing(amount));
	}

}
