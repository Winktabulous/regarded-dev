package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions.AmmunitionFletchingData;
import com.zenyte.game.content.skills.fletching.actions.AmmunitionFletching;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 19:10:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AmmunitionFletchingD extends SkillDialogue {

	private final AmmunitionFletchingData data;

	public AmmunitionFletchingD(final Player player, final AmmunitionFletchingData data) {
		super(player, "How many sets of " + data.getType().getSets() + "?", data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new AmmunitionFletching(data, amount, true));
		}
	}

}
