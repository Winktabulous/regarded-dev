package com.zenyte.game.world.entity.player.action.combat.magic;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public final class GauntletStaffSpell extends MagicCombat {
	public GauntletStaffSpell(final Entity target, final CombatSpell spell, final CastType type) {
		super(target, spell, type);
	}

	@Override
	protected int baseDamage() {
		if (spell == CombatSpell.CRYSTAL_STAFF) {
			final int staff = player.getEquipment().getId(EquipmentSlot.WEAPON);
			switch (staff) {
				case ItemId.CRYSTAL_STAFF_BASIC:
					return 23;
				case ItemId.CRYSTAL_STAFF_ATTUNED:
					return 31;
				case ItemId.CRYSTAL_STAFF_PERFECTED:
					return 39;
			}
		} else if (spell == CombatSpell.CORRUPTED_STAFF) {
			final int staff = player.getEquipment().getId(EquipmentSlot.WEAPON);
			switch (staff) {
				case ItemId.CORRUPTED_STAFF_BASIC:
					return 23;
				case ItemId.CORRUPTED_STAFF_ATTUNED:
					return 31;
				case ItemId.CORRUPTED_STAFF_PERFECTED:
					return 39;
			}
		}

		return 20;
	}

	@Override
	protected int attackSpeed() {
		return 3;
	}

	@Override
	protected int getAttackDistance() {
		if (player.getCombatDefinitions().getStyle() == 3) {
			return 8;
		}
		return 6;
	}

}
