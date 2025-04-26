package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.PotteryFiringData;
import com.zenyte.game.content.skills.crafting.actions.PotteryFiringCrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 27 aug. 2018 | 18:47:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PotteryFiringD extends SkillDialogue {
	public PotteryFiringD(Player player) {
		super(player, PotteryFiringData.POT.getProduct(), PotteryFiringData.PIE_DISH.getProduct(), PotteryFiringData.BOWL.getProduct(), PotteryFiringData.EMPTY_PLANT_POT.getProduct(), PotteryFiringData.POT_LID.getProduct());
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.PotteryFiringData data = PotteryFiringData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new PotteryFiringCrafting(data, amount));
		}
	}
}
