package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;

public class BloodEffect implements SpellEffect {

	@Override
	public void spellEffect(final Entity entity, final Entity target, final int damage) {
		double percentage = 0.25;
		if (entity instanceof final Player player) {
			final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
			percentage += 0.015 * EquipmentUtils.bloodBarkPces(player);
			if (CombatUtilities.isWieldingZurielsStaff(weapon)) {
				percentage *= 1.5;
			} else if (weapon == ItemId.ANCIENT_SCEPTRE || weapon == ItemId.BLOOD_ANCIENT_SCEPTRE_28260) {
				percentage *= 1.1;
			}
		}

		final int heal = (int) (damage * percentage);
		if (heal > 0) {
			if (heal > 9 && entity instanceof Player player) {
				final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
				if (weapon == ItemId.BLOOD_ANCIENT_SCEPTRE_28260) {
					final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
					final int currentHealth = player.getSkills().getLevel(SkillConstants.HITPOINTS);
					final int max = (hp + ((int) (hp * 0.1)));
					if (currentHealth > max + heal)
						return;
					player.setHitpoints(Math.min((currentHealth + heal), max));
				}
			}
			else
				entity.heal(heal);
		}
	}

}