package com.zenyte.game.world.entity;

public abstract class HitBar {

	public abstract int getType();

	public abstract int getPercentage();

	public int getDelay() {
		return 0;
	}

	public int interpolateTime() {
		return 0;
	}

	public int interpolatePercentage() {
		return 0;
	}

	public int getDisplayPercentage() {
		if(forcePercentage != -1) {
			return forcePercentage;
		}
		return 0;
	}

	public boolean hasForcedPercentage() {
		return forcePercentage != -1;
	}

	private int forcePercentage = -1;
	public void setForcePercentage(AbstractEntity entity, int num, int denom){
		forcePercentage = (int) Math.floor(((double) num / denom) * 100);
	}
}