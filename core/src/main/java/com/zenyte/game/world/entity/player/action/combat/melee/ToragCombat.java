package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.content.minigame.barrows.Barrows;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.calog.CAType;

import static com.zenyte.game.world.entity.player.action.combat.CombatUtilities.TORAGS_SET_GFX;

/**
 * @author Kris | 2. juuni 2018 : 22:44:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ToragCombat extends MeleeCombat {
    public ToragCombat(final Entity target) {
        super(target);
    }

    @Override
    protected void extra(final Hit hit) {
        super.extra(hit);
        if (hit.getDamage() <= 0 || Utils.random(3) != 0 || !CombatUtilities.hasFullBarrowsSet(player, "Torag's")) return;
        target.setGraphics(TORAGS_SET_GFX);
        if (!(target instanceof Player)) return;
        final Player p2 = (Player) target;
        final int targetEnergy = (int) p2.getVariables().getRunEnergy();
        final int siphon = (int) (targetEnergy * 0.2F);
        p2.getVariables().setRunEnergy(p2.getVariables().getRunEnergy() - siphon);
    }
}
