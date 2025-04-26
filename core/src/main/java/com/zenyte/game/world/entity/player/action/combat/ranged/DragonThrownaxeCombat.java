package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 2. juuni 2018 : 04:36:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DragonThrownaxeCombat extends RangedCombat {
	public DragonThrownaxeCombat(final Entity target, final AmmunitionDefinition defs) {
		super(target, defs);
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
		if (ammunition == null) {
			return;
		}
		final EquipmentSlot slot = ammunition.isWeapon() ? EquipmentSlot.WEAPON : EquipmentSlot.AMMUNITION;
		final int slotId = slot.getSlot();
		final Item ammo = player.getEquipment().getItem(slotId);
		if (ammo == null) {
			return;
		}
		final int dropChance = getAmmunitionDropChance();
		final int roll = Utils.random(100);
		final Equipment equipment = player.getEquipment();
		final boolean destroyAmmo = (ammunition == AmmunitionDefinitions.DRAGON_THROWNAXE && player.getTemporaryAttributes().get("dragonThrownaxe") != null);
		if (destroyAmmo || destroy || roll <= BREAK_CHANCE || roll <= (BREAK_CHANCE + dropChance)) {
			final int ammoAmount = ammo.getAmount();
			if (ammoAmount > 1) {
				ammo.setAmount(ammoAmount - 1);
			} else {
				equipment.set(slot, null);
			}
			equipment.refresh(slotId);
			if (destroy || roll < BREAK_CHANCE) {
				return;
			}
		}
		if (roll <= (BREAK_CHANCE + dropChance)) {
			final Location location = new Location(target.getLocation());
			if (player.getDuel() != null && player.getDuel().inDuel()) {
				return;
			}
			WorldTasksManager.schedule(() -> {
				final Item item = new Item(ammo.getId());
				final Duel duel = player.getDuel();
				if (duel != null) {
					duel.getAmmunitions().get(player).add(item);
				} else {
					World.spawnFloorItem(item, !World.isFloorFree(location, 1) ? new Location(player.getLocation()) : location, 20, player, player, 300, 500);
				}
			}, delay);
		}
	}
}
