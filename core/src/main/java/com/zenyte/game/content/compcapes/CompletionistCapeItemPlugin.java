package com.zenyte.game.content.compcapes;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

@SuppressWarnings("unused")
public class CompletionistCapeItemPlugin extends ItemPlugin {

	@Override
	public void handle() {
		bind("Wear", (player, item, slotId) -> {
			int compCapeTier = CompletionistCape.isCompletionistCape(item.getId());
			if (compCapeTier > 0) {
				int applicableTier = CompletionistCape.checkRequirements(player);
				if (compCapeTier > applicableTier && !player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
					CompletionistCape.noRequirements(player);
					return;
				}
			}

			player.getEquipment().wear(slotId);
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {32243, 32245, 32247, 32249, 32251, 32253, 32255, 32257, 32259, 32261, 32263, 32265};
	}

}
