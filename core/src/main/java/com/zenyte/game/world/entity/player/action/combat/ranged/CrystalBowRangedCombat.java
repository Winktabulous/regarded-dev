package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;

/**
 * @author Kris | 1. juuni 2018 : 22:44:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CrystalBowRangedCombat extends RangedCombat {

	public CrystalBowRangedCombat(final Entity target, final AmmunitionDefinition defs) {
		super(target, defs);
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
		player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
	}
}
