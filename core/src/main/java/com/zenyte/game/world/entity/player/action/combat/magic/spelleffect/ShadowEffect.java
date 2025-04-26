package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class ShadowEffect extends DebuffEffect {

	public ShadowEffect(int percent) {
		super(SkillConstants.ATTACK, percent, 0);
	}

	@Override
	public void spellEffect(Entity entity, Entity target, int damage) {
		int amount = getPercentage();
		var applyAncientShadowDrain = false;
		if (entity instanceof final Player player) {
			final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
			if (CombatUtilities.isWieldingZurielsStaff(weaponId))
				amount *= 2;
			else if (weaponId == ItemId.ANCIENT_SCEPTRE || weaponId == ItemId.SHADOW_ANCIENT_SCEPTRE_28266)
				amount *= 1.1;
			if (weaponId == ItemId.SHADOW_ANCIENT_SCEPTRE_28266)
				applyAncientShadowDrain = true;
		}

		target.drainSkill(getSkill(), amount, getMinimumDrain());
		// if the weapon is the Shadow Sceptre
		// apply drain to Str, and Def as well
		if (applyAncientShadowDrain) {
			target.drainSkill(SkillConstants.STRENGTH, amount, getMinimumDrain());
			target.drainSkill(SkillConstants.DEFENCE, amount, getMinimumDrain());
			((Player) target).sendMessage("An ancient shadow rushes through you");
		}
	}

}
