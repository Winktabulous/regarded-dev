package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.herblore.actions.Combine;
import com.zenyte.game.content.skills.herblore.actions.Combine.HerbloreData;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

public class HerbloreD extends SkillDialogue {

	private final HerbloreData data;

	public HerbloreD(Player player, HerbloreData data) {
		super(player, data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new Combine(data, amount));
		}
	}

}
