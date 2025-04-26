package com.zenyte.game.content.minigame.barrows.wights;

import com.zenyte.game.content.minigame.barrows.BarrowsWightNPC;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 29. sept 2018 : 05:19:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class VeracTheDefiled extends BarrowsWightNPC implements Spawnable, CombatScript {
	private static final Graphics VERACS_GFX = new Graphics(1041);

	public VeracTheDefiled(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		HitType type = HitType.MELEE;
		int max = combatDefinitions.getMaxHit();
		if (Utils.random(3) == 0) {
			target.setGraphics(VERACS_GFX);
			if (target instanceof Player) {
				type = HitType.DEFAULT;
				final Player player = (Player) target;
				if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
					max *= 0.667F;
				}
			}
			animate();
			final Hit hit = new Hit(this, Utils.random(max), type);
			delayHit(0, target, hit);
			return combatDefinitions.getAttackSpeed();
		}
		animate();
		executeMeleeHit(target, type, max);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1677:
			case 16057: {
				return true;
			}
			default:
				return false;
		}
	}

}
