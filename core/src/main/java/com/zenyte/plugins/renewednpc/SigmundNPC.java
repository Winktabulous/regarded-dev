package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class SigmundNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Respected Premium Supplies"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16032 };
	}

}
