package com.near_reality.game.content.boss.nex;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class FumusNPC extends NexMinion {

	public static final int ID = NpcId.FUMUS;

	protected FumusNPC(Location tile, Direction facing, NexNPC nex) {
		super(ID, tile, facing, nex);
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		NexNPC nex = getNex();
		if (nex != null) {
			nex.drag();
			nex.switchStage(NexStage.SHADOW_SWITCH);
		}
	}

	@Override
	public CombatSpell getSpell() {
		return CombatSpell.SMOKE_BARRAGE;
	}

}
