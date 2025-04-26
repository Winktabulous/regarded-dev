package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;

/**
 * @author Kris | 22/01/2019 18:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StarterBowRangedCombat extends RangedCombat {

    public StarterBowRangedCombat(final Entity target, final AmmunitionDefinitions defs) {
        super(target, defs);
    }

    @Override
    protected void dropAmmunition(final int delay, final boolean destroy) {
        player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
    }

    @Override
    protected boolean outOfAmmo() {
        return player.getWeapon().getCharges() <= 0;
    }

}

