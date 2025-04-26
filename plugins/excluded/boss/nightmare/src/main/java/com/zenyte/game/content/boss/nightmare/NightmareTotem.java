package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType;
import com.zenyte.game.world.entity.player.Player;

import java.util.EnumSet;

public abstract class NightmareTotem extends NPC {

	private int maxCharge;
	private final BaseNightmareNPC boss;

	public NightmareTotem(int id, Location tile, BaseNightmareNPC boss) {
		super(id, tile, Direction.SOUTH, 0);
		this.boss = boss;
		this.hitBar = new EntityHitBar(this) {
			@Override
			public int getType() {
				return 36;
			}

			@Override
			public int getPercentage() {
				float hp = getEntity().getHitpoints();
				final float maxHp = getEntity().getMaxHitpoints();
				if (maxHp == 0) {
					return 0;
				}
				if (hp > maxHp) {
					hp = maxHp;
				}
				final int multiplier = getMultiplier();
				final float mod = maxHp / (multiplier);
				return Math.max(0, Math.min(multiplier - (int) ((hp + mod) / mod), multiplier));
			}
		};
	}

	@Override
	protected void updateCombatDefinitions() {
		super.updateCombatDefinitions();
		combatDefinitions.setImmunityTypes(EnumSet.of(ImmunityType.VENOM, ImmunityType.POISON));
	}

	@Override
	public void applyHit(Hit hit) {
		final int damage = hit.getDamage();
		if (damage > 0) {
			if (hit.getSource() instanceof NightmareParasiteNPC) {
				hit.setHitType(HitType.DISCHARGE);
			} else {
				if (hit.getHitType() == HitType.MAGIC) {
					hit.setDamage(damage * 3);
				}
				hit.setHitType(HitType.CHARGE);
			}
		}

		super.applyHit(hit);
	}

	@Override
	public float getXpModifier(Hit hit) {
		return 0.0f;
	}

	@Override
	public void processHit(final Hit hit) {
		if (isImmune(hit.getHitType())) {
			hit.setDamage(0);
		}
		if (hit.getDamage() > Short.MAX_VALUE) {
			hit.setDamage(Short.MAX_VALUE);
		}
		if (hit.getDamage() > getHitpoints() && hit.getHitType() != HitType.DISCHARGE) {
			hit.setDamage(getHitpoints());
		}
		getUpdateFlags().flag(UpdateFlag.HIT);
		getNextHits().add(hit);
		addHitbar();
		if (hit.getHitType() == HitType.DISCHARGE) {
			heal(hit.getDamage());
		} else {
			removeHitpoints(hit);
		}

		boss.updateTotemHud();
	}

	public void setMaxCharge(int maxCharge) {
		this.maxCharge = maxCharge;
	}

	@Override
	public int getMaxHitpoints() {
		return maxCharge;
	}

	@Override
	public boolean canAttack(Player source) {
		return getId() == activeId();
	}

	@Override
	public void autoRetaliate(Entity source) {
		/* empty */
	}

	@Override
	protected boolean isMovableEntity() {
		return false;
	}

	@Override
	public void sendDeath() {
		if (id != activeId()) return;

		setTransformation(chargedId());
		boss.forEachPlayers(player -> player.sendMessage(chargedMessage()));
		boss.totemAttack();
	}

	@Override
	public void heal(int amount) {
		super.heal(amount);

		if (id == chargedId() && getHitpoints() > 0) {
			setTransformation(activeId());
		}
	}

	public abstract int chargedId();

	public abstract int activeId();

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		return false;
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean isCycleHealable() {
		return false;
	}

	public abstract String chargedMessage();

}
