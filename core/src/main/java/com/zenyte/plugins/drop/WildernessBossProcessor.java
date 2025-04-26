package com.zenyte.plugins.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import net.runelite.api.ItemID;

public class WildernessBossProcessor extends DropProcessor {
	@Override
	public void attach() {
		//appendDrop(new DisplayedDrop(ItemID.BLOOD_MONEY, 1, 3, 1));
	}

	@Override
	public void onDeath(NPC npc, Player killer) {
		//npc.dropItem(killer, new Item(ItemID.BLOOD_MONEY, Utils.random(1, 3)));
	}

	@Override
	public int[] ids() {
		return new int[] {
				NpcId.CALLISTO, NpcId.SCORPIA, NpcId.CHAOS_FANATIC, NpcId.CRAZY_ARCHAEOLOGIST, NpcId.VETION, NpcId.CHAOS_ELEMENTAL,
				NpcId.VENENATIS
		};
	}
}