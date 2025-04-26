package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.content.boons.impl.FourSure;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 21. juuni 2018 : 17:52:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ScytheOfViturCombat extends MeleeCombat {
	public ScytheOfViturCombat(final Entity target) {
		super(target);
	}

	private Direction direction;

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
			((PlayerCombatPlugin) area).onAttack(player, target, "Melee", null, false);
		}
		addAttackedByDelay(player, target);
		sendSoundEffect();
		final Item weapon = player.getWeapon();
		final boolean christmasScythe = player.getEquipment().getId(EquipmentSlot.WEAPON) == ChristmasConstants.CHRISTMAS_SCYTHE;
		boolean hasBoon = player.hasBoon(FourSure.class);
		if (hasBoon || (weapon != null && weapon.getCharges() > 0 || christmasScythe)) {
			specialAttack();
		} else {
			final Hit hit = getHit(player, target, 1, 1, 1, false);
			extra(hit);
			addPoisonTask(hit.getDamage(), 0);
			delayHit(0, hit);
		}
		animate();
		if (!christmasScythe) {
			if(!hasBoon)
				player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
		}
		resetFlag();
		checkIfShouldTerminate(HitType.MELEE);
		return getSpeed();
	}

	private void specialAttack() {
		WorldTasksManager.schedule(() -> {
			final Location middle = target.getLocation();
			final int direction = (DirectionUtil.getFaceDirection((middle.getX() + (target.getSize() / 2.0F)) - player.getX(), (middle.getY() + (target.getSize() / 2.0F)) - player.getY()) + ((target.getSize() & 1) == 0 ? 128 : 0)) & 2047;
			if (direction > 256) {
				if (direction < 768) {
					this.direction = Direction.WEST;
					World.sendGraphics(SpecialAttackScript.SWEEP_DRAGON_WEST_GFX, new Location(player.getX() - 1, player.getY(), player.getPlane()));
				} else if (direction < 1280) {
					this.direction = Direction.NORTH;
					World.sendGraphics(SpecialAttackScript.SWEEP_DRAGON_SOUTH_GFX, new Location(player.getX(), player.getY() + 1, player.getPlane()));
				} else if (direction < 1792) {
					this.direction = Direction.EAST;
					World.sendGraphics(SpecialAttackScript.SWEEP_DRAGON_EAST_GFX, new Location(player.getX() + 1, player.getY(), player.getPlane()));
				} else {
					this.direction = Direction.SOUTH;
					World.sendGraphics(SpecialAttackScript.SWEEP_DRAGON_NORTH_GFX, new Location(player.getX(), player.getY() - 1, player.getPlane()));
				}
			} else {
				this.direction = Direction.SOUTH;
				World.sendGraphics(SpecialAttackScript.SWEEP_DRAGON_NORTH_GFX, new Location(player.getX(), player.getY() - 1, player.getPlane()));
			}
			final Set<Entity> targets = getMultiAttackTargets(player);
			int hitcount = 0;
			final boolean christmasScythe = player.getEquipment().getId(EquipmentSlot.WEAPON) == ChristmasConstants.CHRISTMAS_SCYTHE;
			for (final Entity t : targets) {
				hitcount++;
				final Hit hit = getHit(player, t, 1, hitcount == 1 ? 1.0F : hitcount == 2 ? 0.5F : 0.25F, 1, false);
				if (hit.getDamage() > 0) {
					extra(hit);
					addPoisonTask(hit.getDamage(), -1);
				}
				delayHit(t, -1, hit);
				if (christmasScythe) {
					break;
				}
			}
			if (christmasScythe) {
				return;
			}
			int maxHits = 3;
			if(player.hasBoon(FourSure.class)) {
				maxHits = 4;
			}

			if (hitcount < maxHits) {
				for (final Entity t : targets) {
					if (t.getSize() == 1) {
						continue;
					}
					while (hitcount++ < maxHits) {
						final Hit hit = getHit(player, t, 1, hitcount == 1 ? 1.0F : hitcount == 2 ? 0.5F : 0.25F, 1, false);
						if (hit.getDamage() > 0) {
							extra(hit);
							addPoisonTask(hit.getDamage(), -1);
						}
						delayHit(t, -1, hit);
					}
					break;
				}
			}
		});
	}

	@Override
	public Set<Entity> getMultiAttackTargets(final Player player, final int maxDistance, final int maxAmtTargets) {
		final Set<Entity> possibleTargets = new HashSet<>();
		possibleTargets.add(target);
		final List<Entity> targets = player.getPossibleTargets(EntityType.NPC);
		final Location middle = target.getMiddleLocation();
		final int x = middle.getX();
		final int y = middle.getY();
		final int z = middle.getPlane();
		final int centerHash = middle.getPositionHash();
		final int leftPositionHash = direction == Direction.SOUTH ? Location.hash(x + 1, y, z) : direction == Direction.WEST ? Location.hash(x, y - 1, z) : direction == Direction.NORTH ? Location.hash(x - 1, y, z) : Location.hash(x, y + 1, z);
		final int rightPositionHash = direction == Direction.SOUTH ? Location.hash(x - 1, y, z) : direction == Direction.WEST ? Location.hash(x, y + 1, z) : direction == Direction.NORTH ? Location.hash(x + 1, y, z) : Location.hash(x, y - 1, z);
		for (final Entity t : targets) {
			if (possibleTargets.size() >= 3) {
				break;
			}
			final int hash = t.getLocation().getPositionHash();
			if (hash == leftPositionHash || hash == rightPositionHash || hash == centerHash) {
				possibleTargets.add(t);
			}
		}
		return possibleTargets;
	}
}
