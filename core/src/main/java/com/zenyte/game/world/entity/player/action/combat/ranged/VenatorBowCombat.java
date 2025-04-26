package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;

import java.util.List;
import java.util.Optional;

/**
 * @author Savions.
 */
public class VenatorBowCombat extends RangedCombat {

	private static final Animation SWING_ANIM = new Animation(9858);
	private static final Graphics SWING_GFX = new Graphics(2289);
	private static final Projectile SWING_PROJECTILE = new Projectile(2291, 39, 36, 31, 10, 20, 11, 2);
	public VenatorBowCombat(Entity target, AmmunitionDefinition defs) {
		super(target, defs);
	}

	@Override public int processAfterMovement() {
		if (!isWithinAttackDistance()) {
			return 0;
		}
		if (!canAttack()) {
			return -1;
		}
		addAttackedByDelay(player, target);
		final RegionArea area = player.getArea();
		if (area instanceof PlayerCombatPlugin) {
			((PlayerCombatPlugin) area).onAttack(player, target, "Ranged", null, false);
		}
		final int ticks = this.fireProjectile(SWING_PROJECTILE);
		animate();
		dropAmmunition(ticks, !ammunition.isRetrievable());
		resetFlag();
		final Hit hit = getHit(player, target, 1, 1, 1, false);
		if (hit.getDamage() > 0) {
			addPoisonTask(ticks);
		}
		player.setAnimation(SWING_ANIM);
		player.setGraphics(SWING_GFX);
		delayHit(ticks, hit);
		if (player.isMultiArea()) {
			final Item item = player.getEquipment().getItem(EquipmentSlot.WEAPON);
			if (item != null && item.getCharges() > 0) {
				attemptBounce(ticks, target, hit, 0);
			}
		}
		checkIfShouldTerminate(HitType.RANGED);
		return getWeaponSpeed();
	}

	private void attemptBounce(final int ticks, final Entity currentTarget, final Hit originalHit, int count) {
		if (count >= 2) {
			return;
		}
		final Optional<Entity> bounceTarget = findBounceTarget(currentTarget);
		if (bounceTarget.isPresent()) {
			final Entity newTarget = bounceTarget.get();
			final Projectile bounceProjectile = new Projectile(2007, 31, 31, 0, 10, 30, 0, 0);
			bounceProjectile.setDelay(SWING_PROJECTILE.getDelay() + 30 * ticks);
			final int additionalTicks = currentTarget.getLocation().getTileDistance(newTarget.getLocation()) - 1;
			World.sendProjectile(new Location(currentTarget.getLocation()), newTarget, bounceProjectile);
			final int newTicks = ticks + additionalTicks;
			delayHit(newTarget, newTicks, new Hit(player, (int) (originalHit.getDamage() * 0.66), HitType.RANGED));
			attemptBounce(additionalTicks == 0 ? newTicks + 1 : newTicks, newTarget, originalHit, count + 1);
		}
	}
	private Optional<Entity> findBounceTarget(final Entity currentTarget) {
		final List<Entity> possibleTargets = player.getPossibleTargets(Entity.EntityType.BOTH);
		possibleTargets.remove(currentTarget);
		possibleTargets.removeIf(e -> e instanceof Player && !player.canHit((Player) e));
		possibleTargets.removeIf(e -> e.isDead() || !e.isMultiArea());
		possibleTargets.removeIf(e -> {
			final Location location = e.getSize() % 2 == 0 ? e.getLocation().transform(e.getSize() - 1, e.getSize() - 1) : e.getMiddleLocation();
			return !currentTarget.getLocation().withinDistance(location, 2);
		});
		return possibleTargets.stream().findFirst();
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
		super.dropAmmunition(delay, destroy);
		player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
		final Item item = player.getEquipment().getItem(EquipmentSlot.WEAPON);
		if (item == null) {
			return;
		}
		final int charges = item.getCharges();
		if (charges > 0 && (charges % 500 == 0 || charges == 100 || charges == 50)) {
			player.getChargesManager().notifyPlayerOfChargesLeft(item);
		}
	}
}
