package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class PrimulaNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Herblore Store"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16034 };
	}

}
