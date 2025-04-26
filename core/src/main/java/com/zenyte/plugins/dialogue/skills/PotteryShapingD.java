package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.PotteryShapingData;
import com.zenyte.game.content.skills.crafting.actions.PotteryShapingCrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 27 aug. 2018 | 18:57:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PotteryShapingD extends SkillDialogue {
	public PotteryShapingD(Player player) {
		super(player, PotteryShapingData.POT.getProduct(), PotteryShapingData.PIE_DISH.getProduct(), PotteryShapingData.BOWL.getProduct(), PotteryShapingData.EMPTY_PLANT_POT.getProduct(), PotteryShapingData.POT_LID.getProduct());
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.PotteryShapingData data = PotteryShapingData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new PotteryShapingCrafting(data, amount));
		}
	}
}
