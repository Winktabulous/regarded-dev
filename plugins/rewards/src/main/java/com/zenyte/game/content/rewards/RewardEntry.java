package com.zenyte.game.content.rewards;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;

public class RewardEntry {

    private int itemId;
    private int minAmount;
    private int maxAmount;


    public RewardEntry(Item item) {
        this.itemId = item.getId();
        this.minAmount = item.getAmount();
        this.maxAmount = item.getAmount();
    }

    public RewardEntry(int itemId, int minAmount, int maxAmount) {
        this.itemId = itemId;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    /**
     * Gets a random amount ranging from {@link #minAmount} {@link #maxAmount}
     * @return - the a new random amount
     */
    public int getRandomAmount() {
        if (maxAmount == minAmount) return maxAmount;
        int difference = maxAmount - minAmount;
        return Utils.random(difference) + minAmount;
    }

    public Item getRandomItem() {
        return new Item(itemId, getRandomAmount());
    }

    public int getItemId() {
        return itemId;
    }

    public RewardEntry setItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public RewardEntry setMinAmount(int minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public RewardEntry setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
        return this;
    }


}