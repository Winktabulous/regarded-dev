package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;

/**
 * An immutable class that stores item id, minimum amount, maximum amount 
 * and if necessary, the rarity.
 * @author Kris | 21. okt 2017 : 12:50.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class ImmutableItem {

    private final int id, minAmount, maxAmount;
    private final double rate;

    private double overrideRateDisplay = 0;

	public ImmutableItem(final int id) {
		this(id, 1, 1, 0);
	}
	
	public ImmutableItem(final int id, final int amount) {
		this(id, amount, amount, 0);
	}
	
	public ImmutableItem(final int id, final int minAmount, final int maxAmount) {
		this(id, minAmount, maxAmount, 0);
	}

	public ImmutableItem(final int id, final int minAmount, final int maxAmount, final double rate) {
		this.id = id;
		this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rate = rate;
    }

    public ImmutableItem(final int id, final int minAmount, final int maxAmount, final double rate, final double overrideRateDisplay) {
        this.id = id;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rate = rate;
        this.overrideRateDisplay = overrideRateDisplay;
    }


    public Item generateResult() {
        return new Item(getId(), Utils.random(getMinAmount(), getMaxAmount()));
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

    public double getRate() {
        return rate;
    }

    public double getRateWithOverride() {
        if(overrideRateDisplay != 0)
            return overrideRateDisplay;
        return rate;
    }

}
