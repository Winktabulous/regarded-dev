package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions.BattlestaffAttachingData;
import com.zenyte.game.content.skills.crafting.actions.BattlestaffAttachingCrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 20:25:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BattlestaffAttachingD extends SkillDialogue {

	private final BattlestaffAttachingData data;

	public BattlestaffAttachingD(final Player player, final BattlestaffAttachingData data) {
		super(player, data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new BattlestaffAttachingCrafting(data, amount));
		}
	}

}
