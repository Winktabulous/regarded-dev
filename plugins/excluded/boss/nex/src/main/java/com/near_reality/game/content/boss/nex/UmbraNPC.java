package com.near_reality.game.content.boss.nex;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class UmbraNPC extends NexMinion {

	public static final int ID = NpcId.UMBRA;

	protected UmbraNPC(Location tile, Direction facing, NexNPC nex) {
		super(ID, tile, facing, nex);
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		NexNPC nex = getNex();
		if (nex != null) {
			nex.stopShadowEmbrace();
			nex.switchStage(NexStage.BLOOD_SWITCH);
		}
	}

	@Override
	public CombatSpell getSpell() {
		return CombatSpell.SHADOW_BARRAGE;
	}

}
