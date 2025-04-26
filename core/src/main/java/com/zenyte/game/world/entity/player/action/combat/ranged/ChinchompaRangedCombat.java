package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.google.common.base.Preconditions;
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;

/**
 * @author Kris | 1. juuni 2018 : 00:14:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ChinchompaRangedCombat extends RangedCombat {
	public ChinchompaRangedCombat(final Entity target, final AmmunitionDefinition defs) {
		super(target, defs);
	}

	private static final int SHORT_FUSE = 0;
	private static final int MEDIUM_FUSE = 1;
	private static final int LONG_FUSE = 3;

	@Override
	public int processAfterMovement() {
		if (!isWithinAttackDistance()) {
			return 0;
		}
		if (!canAttack()) {
			return -1;
		}
		final RegionArea area = player.getArea();
		if (area instanceof PlayerCombatPlugin) {
			((PlayerCombatPlugin) area).onAttack(player, target, "Ranged", null, false);
		}
		addAttackedByDelay(player, target);
		final int delayUntilImpact = fireProjectile();
		WorldTasksManager.schedule(() -> target.setGraphics(CombatUtilities.CHINCHOMPA_GFX), delayUntilImpact);
		player.setAnimation(CombatUtilities.CHINCHOMPA_THROW_ANIM);
		final Projectile projectile = this.ammunition.getProjectile();
		final int clientCycles = projectile.getProjectileDuration(player.getLocation(), target.getLocation());
		if (ammunition.getSoundEffect() != null) {
			player.getPacketDispatcher().sendSoundEffect(ammunition.getSoundEffect());
		}
		player.getPacketDispatcher().sendSoundEffect(new SoundEffect(360, 0, clientCycles));
		final float accMod = getAccuracyModifier();
		resetFlag();
		final Hit hit = getHit(player, target, accMod, 1, 1, false);
		delayHit(target, delayUntilImpact, hit);
		final int damage = hit.getDamage();
		if (damage > 0) {
			addPoisonTask(delayUntilImpact);
		}
		attackTarget(getMultiAttackTargets(player), originalTarget -> {
			if (this.target == originalTarget) {
				return true;
			}
			//Chinchompa hits are rolled against the original target's defence.
			final Hit otherHit = getHit(player, originalTarget, accMod, 1, 1, false);
			if (damage == 0) {
				otherHit.setDamage(0);
			}
			otherHit.putAttribute("chin_multi_hit", true);
			delayHit(target, delayUntilImpact, otherHit);
			return true;
		});
		dropAmmunition(delayUntilImpact, true);
		checkIfShouldTerminate(HitType.RANGED);
		return getWeaponSpeed();
	}

	private final float getAccuracyModifier() {
		final int distance = (int) player.getLocation().getDistance(target.getLocation());
		final int style = player.getCombatDefinitions().getStyle();
		Preconditions.checkArgument(style == SHORT_FUSE || style == MEDIUM_FUSE || style == LONG_FUSE);
		float accuracyModifier = 1.0F;
		if (style == SHORT_FUSE) {
			if (distance >= 7) {
				accuracyModifier = 0.5F;
			} else if (distance >= 4) {
				accuracyModifier = 0.75F;
			}
		} else if (style == MEDIUM_FUSE) {
			if (distance <= 3 || distance >= 7) {
				accuracyModifier = 0.75F;
			}
		} else {
			if (distance <= 3) {
				accuracyModifier = 0.5F;
			} else if (distance <= 6) {
				accuracyModifier = 0.75F;
			}
		}
		return accuracyModifier;
	}
}
