package com.zenyte.game.world.entity.player.action.combat.magic;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;

public class HarmonisedStaffCombat extends MagicCombat {

	public HarmonisedStaffCombat(final Entity target, final CombatSpell spell, final CastType castType) {
		super(target, spell, castType);
	}

	@Override
	protected int attackSpeed() {
		if (spell.getSpellbook().equals(Spellbook.NORMAL))
			return 3;
		return 4;
	}

	@Override
	public boolean checkAutocastDelay() {
		return false;
	}

}
