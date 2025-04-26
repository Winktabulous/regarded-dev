package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 1. juuni 2018 : 03:34:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BlowpipeRangedCombat extends RangedCombat {
	public BlowpipeRangedCombat(final Entity target, final AmmunitionDefinition defs) {
		super(target, defs);
	}

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
		if (player.getCombatDefinitions().isUsingSpecial()) {
			final int delay = useSpecial(player, SpecialType.RANGED);
			if (delay != -1) {
				return 1 + getWeaponSpeed();
			}
		}
		if (ammunition.getSoundEffect() != null) {
			player.getPacketDispatcher().sendSoundEffect(ammunition.getSoundEffect());
		}
		final int ticks = this.fireProjectile();
		animate();
		dropAmmunition(ticks, false);
		addPoisonTask(ticks);
		resetFlag();
		final Hit hit = getHit(player, target, 1, 1, 1, false);
		extra(hit);
		delayHit(ticks, hit);
		drawback();
		checkIfShouldTerminate(HitType.RANGED);
		return getWeaponSpeed();
	}

	@Override
	protected int getWeaponSpeed() {
		final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
		if (weapon == null) {
			return 5;
		}
		final ItemDefinitions defs = weapon.getDefinitions();
		if (defs == null) {
			return 5;
		}
		int speed = (ammunition.getProjectile() != null && player.getCombatDefinitions().getStyle() == 1 ? 8 : 9) - defs.getAttackSpeed();
		if (weapon.getId() == 12926 && target.getEntityType() == EntityType.PLAYER) {
			speed++;
		}
		return speed;
	}

	@Override
	protected void addPoisonTask(final int delay) {
		if (target instanceof NPC && CombatUtilities.isWearingSerpentineHelmet(player) || Utils.randomBoolean(3)) {
			WorldTasksManager.schedule(() -> target.getToxins().applyToxin(ToxinType.VENOM, 6, player), delay);
			return;
		}

		super.addPoisonTask(delay);
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
		if (ammunition == null) {
			return;
		}
		final Item blowpipe = player.getWeapon();
		if (blowpipe == null) {
			return;
		}
		final Object ammoId = blowpipe.getAttribute("blowpipeDartType");
		if (!(ammoId instanceof Number)) {
			return;
		}
		final int dropChance = getAmmunitionDropChance();
		final int roll = Utils.random(100);
		if (destroy || roll <= BREAK_CHANCE || roll <= (BREAK_CHANCE + dropChance)) {
			player.getChargesManager().removeCharges(blowpipe, 2, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
			if (destroy || roll <= BREAK_CHANCE) {
				return;
			}
		} else {
			player.getChargesManager().removeCharges(blowpipe, 1, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
		}
		if (roll <= (BREAK_CHANCE + dropChance)) {
			final Location location = new Location(target.getLocation());
			if (player.getDuel() != null && player.getDuel().inDuel()) {
				return;
			}
			WorldTasksManager.schedule(() -> World.spawnFloorItem(new Item(((Number) ammoId).intValue()), !World.isFloorFree(location, 1) ? new Location(player.getLocation()) : location, 20, player, player, 300, 500), delay);
		}
	}

	@Override
	protected boolean checkPreconditions() {
		final Item item = player.getWeapon();
		if (item == null) {
			return false;
		}
		final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
		final int scales = item.getNumericAttribute("blowpipeScales").intValue();
		final int dartId = item.getNumericAttribute("blowpipeDartType").intValue();
		if (darts == 0) {
			player.sendMessage("You need to charge your blowpipe with some darts first.");
			return false;
		}
		if (scales == 0) {
			player.sendMessage("You need to charge your blowpipe with some scales first.");
			return false;
		}
		ammunition = AmmunitionDefinitions.getBlowpipeDefinitions(dartId);
		return true;
	}

	@Override
	protected boolean initiateCombat(final Player player) {
		if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned() || player.isFullMovementLocked()) {
			return false;
		}
		if (target.isDead() || target.isFinished() || target.isCantInteract()) {
			return false;
		}
		final Item item = player.getWeapon();
		if (item == null) {
			return false;
		}
		final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
		final int scales = item.getNumericAttribute("blowpipeScales").intValue();
		if (darts == 0) {
			player.sendMessage("Your blowpipe has ran out of darts.");
			return false;
		}
		if (scales == 0) {
			player.sendMessage("Your blowpipe has ran out of scales.");
			return false;
		}
		final int distanceX = player.getX() - target.getX();
		final int distanceY = player.getY() - target.getY();
		final int size = target.getSize();
		if (outOfAmmo()) return false;
		final int viewDistance = player.getViewDistance();
		if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
			return false;
		}
		if (target.getEntityType() == EntityType.PLAYER) {
			if (!player.isCanPvp() || !((Player) target).isCanPvp()) {
				player.sendMessage("You can't attack someone in a safe zone.");
				return false;
			}
		}
		if (player.isFrozen() || player.isMovementLocked(false)) {
			return true;
		}
		if (colliding()) {
			player.getCombatEvent().process();
			return true;
		}
		if (handleDragonfireShields(player, false)) {
			if (!canAttack()) {
				return false;
			}
			handleDragonfireShields(player, true);
			player.getActionManager().addActionDelay(4);
			return true;
		}
		return pathfind();
	}
}
