package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.WeavingData;
import com.zenyte.game.content.skills.crafting.actions.Weaving;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 sep. 2018 | 21:18:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WeavingD extends SkillDialogue {
	public WeavingD(Player player) {
		super(player, WeavingData.BASKET.getProduct(), WeavingData.EMPTY_SACK.getProduct(), WeavingData.DRIFT_NET.getProduct(), WeavingData.STRIP_OF_CLOTH.getProduct());
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.WeavingData data = WeavingData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new Weaving(data, amount));
		}
	}
}
