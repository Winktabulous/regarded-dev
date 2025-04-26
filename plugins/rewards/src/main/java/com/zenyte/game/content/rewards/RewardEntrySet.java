package com.zenyte.game.content.rewards;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RewardEntrySet {

    /**
     * The percentage chance too hit the reward set
     */
    private float percent;


    /**
     * The reward entries
     */
    private List<RewardEntry> entries = new ArrayList<>();

    /**
     * The type of roll
     */
    private RewardSetRollType type;
    private RewardSelectType selectType = RewardSelectType.RANDOM;
    private RewardPercentageStackType stackType = RewardPercentageStackType.NEVER_CHANGE;


    /**
     * Gets the percentage
     * @return
     */
    public float getPercent() {
        return percent;
    }

    /**
     * Attempts a roll
     * @return
     */
    public boolean roll() {
        return roll(0);
    }

    public boolean roll(double boost) {
        double mod = 1D + (boost / 100D);
        float rate = percent / 100F;
        return rate * mod >= (ThreadLocalRandom.current().nextFloat());
    }

    /**
     * Gets a random item from the item list
     * @return
     */
    public Item getRandomItem() {
        return entries.get(Utils.random(entries.size() - 1)).getRandomItem();
    }

    public List<Item> getReward() {
        List<Item> rewards = new ArrayList<>();
        if (selectType == RewardSelectType.ALL_ENTRIES) {
            for (RewardEntry entry : entries) {
                Item item = entry.getRandomItem();
                rewards.add(item);
            }
        } else {
            Item item = getRandomItem();
            rewards.add(item);
        }

        return rewards;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (RewardEntry entry : entries) {
            double rate = ((percent / 100F) * (1F / entries.size())) * 100D;
            Item chancedItem = new Item(entry.getItemId(), 1);
            items.add(chancedItem);
        }
        return items;
    }

    public RewardEntrySet setPercent(float percent) {
        this.percent = percent;
        return this;
    }

    public List<RewardEntry> getEntries() {
        return entries;
    }

    public RewardEntrySet setEntries(List<RewardEntry> entries) {
        this.entries = entries;
        return this;
    }

    public RewardSetRollType getType() {
        return type;
    }

    public RewardEntrySet setType(RewardSetRollType type) {
        this.type = type;
        return this;
    }


    public RewardSelectType getSelectType() {
        return selectType;
    }

    public RewardEntrySet setSelectType(RewardSelectType selectType) {
        this.selectType = selectType;
        return this;
    }


    public RewardPercentageStackType getStackType() {
        return stackType;
    }

    public void setStackType(RewardPercentageStackType stackType) {
        this.stackType = stackType;
    }


}