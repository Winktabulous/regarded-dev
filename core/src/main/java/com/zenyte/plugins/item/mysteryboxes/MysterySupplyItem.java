package com.zenyte.plugins.item.mysteryboxes;

public class MysterySupplyItem {

	private final int id;
	private final int minAmount;
	private final int maxAmount;

	MysterySupplyItem(int id, int minAmount, int maxAmount) {
		this.id = id;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}

	public int getId() {
		return id;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

}
