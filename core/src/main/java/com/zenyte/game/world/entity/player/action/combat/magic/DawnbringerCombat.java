package com.zenyte.game.world.entity.player.action.combat.magic;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;

public final class DawnbringerCombat extends MagicCombat {

	public DawnbringerCombat(final Entity target, final CombatSpell spell, final CastType type) {
		super(target, spell, type);
	}

	@Override
	protected int baseDamage() {
		return (int) Math.max(20, Math.floor((player.getSkills().getLevel(SkillConstants.MAGIC) / 3.0F) - 5));
	}

	protected void extra() {
		if (!player.inArea("The Final Challenge")) {
			player.sendMessage("You cannot use the Dawnbringer outside of the Theatre of Blood.");
			interrupt = true;
		}
	}

	@Override
	protected boolean canAttack() {
		if (!player.inArea("The Final Challenge")) {
			player.sendMessage("You cannot use the Dawnbringer outside of the Theatre of Blood.");
			return false;
		}

		return super.canAttack();
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

	@Override
	public HitType getHitType() {
		return HitType.SHIELD;
	}

}
