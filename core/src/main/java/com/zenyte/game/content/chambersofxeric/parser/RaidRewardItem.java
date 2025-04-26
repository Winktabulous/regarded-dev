package com.zenyte.game.content.chambersofxeric.parser;

public class RaidRewardItem {
    private final String itemName;
    private final int itemAmount;

    public String getItemName() {
        return itemName;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public RaidRewardItem(String itemName, int itemAmount) {
        this.itemName = itemName;
        this.itemAmount = itemAmount;
    }
}
