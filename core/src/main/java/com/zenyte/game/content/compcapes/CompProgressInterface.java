package com.zenyte.game.content.compcapes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class CompProgressInterface extends Interface {

	@Override
	public void open(Player player) {
		player.getVarManager().sendVarInstant(261, CompletionistCape.countFinished(player));
		player.getVarManager().sendVarInstant(262, CompletionistCape.countTotal());

		super.open(player);

		player.awaitInputIntNoClose(amount -> {
			switch (amount) {
				case 0 -> CompletionistCape.showRequirements(player, CompletionistCape.CompletionistCapeRequirementSection.MAX);
				case 1 -> CompletionistCape.showRequirements(player, CompletionistCape.CompletionistCapeRequirementSection.ACHIEVEMENTS);
				case 2 -> CompletionistCape.showRequirements(player, CompletionistCape.CompletionistCapeRequirementSection.TASKS);
				case 3 -> CompletionistCape.showRequirements(player, CompletionistCape.CompletionistCapeRequirementSection.COMBAT_TASKS);
			}
		});
	}

	@Override
	protected void attach() {

	}

	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.COMP_PROGRESS;
	}

}
