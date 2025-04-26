package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

/**
 * @author Kris | 2. juuni 2018 : 20:50:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class VeracCombat extends MeleeCombat {
	private static final Graphics VERACS_GFX = new Graphics(1041);

	public VeracCombat(final Entity target) {
		super(target);
	}

	@Override
	public Hit getHit(final Player player, final Entity target, final double accuracyModifier, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
		final boolean verac = CombatUtilities.hasFullBarrowsSet(player, "Verac's") && Utils.random(3) == 0;
		final int damage = getRandomHit(player, target, getMaxHit(player, passiveModifier, 1, false), verac ? 100 : accuracyModifier);
		final Hit hit = new Hit(player, verac ? (1 + damage) : damage, HitType.MELEE);
		if (verac) {
			target.setGraphics(VERACS_GFX);
			hit.putAttribute("ignore protect melee", true);
		}
		return hit;
	}
}
