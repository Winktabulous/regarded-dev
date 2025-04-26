package com.zenyte.game.world.entity.player.action.combat.magic;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;

/**
 * @author Kris | 3. juuni 2018 : 22:48:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SeasTridentCombat extends MagicCombat {
	public SeasTridentCombat(final Entity target, final CombatSpell spell, final CastType type) {
		super(target, spell, type);
	}

	@Override
	protected int baseDamage() {
		return (int) Math.max(20, Math.floor((player.getSkills().getLevel(SkillConstants.MAGIC) / 3.0F) - 5));
	}

	@Override
	protected void extra(Hit hit) {
		super.extra(hit);
		final Item weapon = player.getWeapon();
		if (weapon == null || weapon.getCharges() <= 0) {
			interrupt = true;
		}
	}

	@Override
	protected int attackSpeed() {
		return 3;
	}

	@Override
	protected boolean canAttack() {
		final Item weapon = player.getWeapon();
		final int charges = weapon.getCharges();
		if (DegradableItem.getDefaultCharges(weapon.getId(), -1) != charges && charges <= 0) {
			player.sendMessage("Your trident is out of charges.");
			return false;
		}
		return super.canAttack();
	}

	@Override
	protected int getAttackDistance() {
		if (player.getCombatDefinitions().getStyle() == 3) {
			return 8;
		}
		return 6;
	}

	@Override
	protected void degrade() {
		player.getChargesManager().removeCharges(DegradeType.TRIDENT);
	}
}
