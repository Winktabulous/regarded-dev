package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

/**
 * @author Kris | 2. juuni 2018 : 20:46:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DinhsBulwarkCombat extends MeleeCombat {

	public DinhsBulwarkCombat(final Entity target) {
		super(target);
	}

	@Override
    public boolean canInitiate() {
        if (player.getCombatDefinitions().getStyle() != 0) {
            player.sendMessage("You cannot attack with the Dinh's bulwark using block!");
            return false;
        }
        return super.canInitiate();
    }

}
