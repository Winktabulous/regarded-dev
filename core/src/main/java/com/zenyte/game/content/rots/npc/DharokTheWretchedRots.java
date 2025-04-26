package com.zenyte.game.content.rots.npc;

import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

public class DharokTheWretchedRots extends RotsBrother implements CombatScript {

	private long lastAttack = 0L;
	public DharokTheWretchedRots(final Location tile, RotsInstance instance) {
		super(16036, tile, instance);
	}

	@Override
	public int attack(final Entity target) {
		if (WorldThread.getCurrentCycle() >= lastAttack && (target.isLocked() || target.isFrozen() || Utils.randomBoolean(11))) {
			if (target.isFrozen() || target.isLocked()) {
				setForceTalk("Thank you brother!");
			} else {
				setForceTalk("Here it comes..");
			}

			final Location locationCopy = target.getLocation().copy();
			freeze(2);
			lastAttack = WorldThread.getCurrentCycle() + 10;
			delay(1, () -> {
				animate();
				if (target.getLocation().matches(locationCopy)) {
					final int maxHealth = getMaxHitpoints();
					final int health = getHitpoints();
					final int max = (int) (29.0F + (29.0F * ((float) (maxHealth - health) / maxHealth)));
					delayHit(0, target, new Hit(this, Utils.random(max), HitType.REGULAR));
				}
			});

			return combatDefinitions.getAttackSpeed() + 2;
		}

		final int maxHealth = getMaxHitpoints();
		final int health = getHitpoints();
		final int max = (int) (29.0F + (29.0F * ((float) (maxHealth - health) / maxHealth)));
		animate();
		executeMeleeHit(target, max);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean isFreezeImmune() {
		return WorldThread.getCurrentCycle() < lastAttack;
	}

}
