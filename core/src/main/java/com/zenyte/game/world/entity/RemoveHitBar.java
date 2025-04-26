package com.zenyte.game.world.entity;

/**
 * @author Savions.
 */
public class RemoveHitBar extends HitBar {

	private final int type;

	public RemoveHitBar(int type) {
		this.type = type;
	}

	@Override public int getType() {
		return type;
	}

	@Override public int getPercentage() {
		return 0;
	}

	@Override public int interpolateTime() {
		return 32767;
	}
}
