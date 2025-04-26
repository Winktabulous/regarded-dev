package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

@SuppressWarnings("unused")
public class ZalcanoGolemDrop extends DropProcessor {

	@Override
	public void attach() {
		appendDrop(new DisplayedDrop(ItemId.IMBUED_TEPHRA, 16, 24, 1));
	}

	@Override
	public void onDeath(NPC npc, Player killer) {
		npc.dropItem(killer, new Item(ItemId.IMBUED_TEPHRA, Utils.random(16, 24)));
		killer.getCombatAchievements().complete(CAType.TEAM_PLAYER);
	}

	@Override
	public int[] ids() {
		return new int[] {ZalcanoConstants.ZALCANO_GOLEM};
	}

}
