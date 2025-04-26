package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;

public class SmokeEffect implements SpellEffect {

	public SmokeEffect(final int damage) {
		this.damage = damage;
	}

	private final int damage;

	@Override
	public void spellEffect(final Entity entity, final Entity target, final int damage) {
		if (Utils.random(3) != 0) {
			return;
		}

		int amount = this.damage;
		var applyAncientSmoke = false;
        if (entity instanceof Player player) {
			final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
			if (CombatUtilities.isWieldingZurielsStaff(player)) {
				amount *= 2;
			}
			else if (weaponId == ItemId.ANCIENT_SCEPTRE || weaponId == ItemId.SMOKE_ANCIENT_SCEPTRE_28264)
				amount += 1;
			if (weaponId == ItemId.SMOKE_ANCIENT_SCEPTRE_28264)
				applyAncientSmoke = true;
		}
		target.getToxins().applyToxin(ToxinType.POISON, amount, entity);

		if (applyAncientSmoke && target.getToxins().isPoisoned()){
			((Player) target).getVariables().schedule(100, TickVariable.ANCIENT_SMOKE);
			((Player) target).sendMessage("Your lungs fill with an ancient toxic smoke...");
		}

	}

}
