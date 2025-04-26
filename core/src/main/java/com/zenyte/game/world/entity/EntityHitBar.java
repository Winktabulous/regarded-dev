package com.zenyte.game.world.entity;

import mgi.types.config.HitbarDefinitions;

public class EntityHitBar extends HitBar {
	public EntityHitBar(final Entity entity) {
		this.entity = entity;
	}

	private final transient Entity entity;

	private int overrideType = -1;

	public void setOverrideType(int type) {
		overrideType = type;
	}

	@Override
	public int getPercentage() {
		if(hasForcedPercentage())
			return getDisplayPercentage();
		float hp = entity.getHitpoints();
		if (hp <= 0) {
			return 0;
		}
		final float maxHp = entity.getMaxHitpoints();
		if (maxHp == 0) {
			return 0;
		}
		if (hp > maxHp) {
			hp = maxHp;
		}
		final int multiplier = getMultiplier();
		final float mod = maxHp / (multiplier);
		return Math.min((int) ((hp + mod) / mod), multiplier);
	}

	@Override
	public int getType() {
		if(overrideType != -1)
			return overrideType;
		switch (getSize()) {
		case 4: 
			return 17;
		case 5: 
			return 18;
		case 6:
		case 7:
			return 20;
		case 8:
		case 9:
			return 22;
		default: 
			return 0;
		}
	}

	public int getMultiplier() {
		final int type = getType();
		return HitbarDefinitions.get(type).getSize();
	}

	protected int getSize() {
		return entity.getSize();
	}

	public Entity getEntity() {
		return entity;
	}

}
