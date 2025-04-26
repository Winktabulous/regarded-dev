package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class TarikNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> {
			if(npc.getId() == 16059)
				player.openShop("Legendary Premium Supplies");
			else
				player.openShop("Uber Premium Supplies");
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16033, 16059 };
	}

}
