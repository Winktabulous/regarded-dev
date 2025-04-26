package com.zenyte.game.world.entity.npc.drop.matrix;

/**
 * JSON Sensitive file, renaming rate broke drops
 */
public class Drop {

	public static final int GUARANTEED_RATE = 100_000;

	private int itemId, minAmount, maxAmount;
	private int rate;
	//private boolean rare;

	public Drop(final Drop drop) {
		this.itemId = drop.itemId;
		this.minAmount = drop.minAmount;
		this.maxAmount = drop.maxAmount;
		this.rate = drop.rate;
	}

	public Drop(int itemId, int rate, int minAmount, int maxAmount/*, boolean rare*/) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		//this.rare = rare;
	}

	public Drop(int itemId, int rate, int minAmount) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = minAmount;
	//	this.rare = false;
	}


	public int getExtraAmount() {
		return maxAmount - minAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(short itemId) {
		this.itemId = itemId;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int amount) {
		this.maxAmount = amount;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(int amount) {
		this.minAmount = amount;
	}
	public int getBaseRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isAlways() {
	    return rate == 100000;
    }

	/*public boolean isFromRareTable() {
		return rare;
	}*/
}