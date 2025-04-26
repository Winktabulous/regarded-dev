package com.zenyte.game.content.pyramidplunder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Collection;

/**
 * @author Christopher
 * @since 4/1/2020
 */

public enum PlunderReward {
    IVORY_COMB(PlunderRewardTier.IVORY, ItemId.IVORY_COMB, 50),

    STONE_SEAL(PlunderRewardTier.STONE, ItemId.STONE_SEAL, 150),
    GOLD_SEAL(PlunderRewardTier.GOLD, ItemId.GOLD_SEAL, 750),

    POTTERY_SCARAB(PlunderRewardTier.POTTERY, ItemId.POTTERY_SCARAB, 75),
    STONE_SCARAB(PlunderRewardTier.STONE, ItemId.STONE_SCARAB, 175),
    GOLD_SCARAB(PlunderRewardTier.GOLD, ItemId.GOLDEN_SCARAB, 1000),

    POTTERY_STATUETTE(PlunderRewardTier.POTTERY, ItemId.POTTERY_STATUETTE, 100),
    STONE_STATUETTE(PlunderRewardTier.STONE, ItemId.STONE_STATUETTE, 200),
    GOLDEN_STATUETTE(PlunderRewardTier.GOLD, ItemId.GOLDEN_STATUETTE, 1250);

    public static final PlunderReward[] rewards = values();
    private static final Multimap<PlunderRewardTier, PlunderReward> rewardsByTier = HashMultimap.create();
    private static final Int2ObjectMap<PlunderReward> rewardsByItemId = new Int2ObjectOpenHashMap<>();

    static {
        for (PlunderReward reward : rewards) {
            rewardsByTier.put(reward.getTier(), reward);
            rewardsByItemId.put(reward.getItemId(), reward);
        }
    }

    private final PlunderRewardTier tier;
    private final int itemId;
    private final int coins;

    /**
     * The multiplier for the amount of coins you get from exchanging your artefacts.
     */
    public static final double REWARD_MULTIPLIER = 2.5;

    public static Collection<PlunderReward> get(final PlunderRewardTier tier) {
        return rewardsByTier.get(tier);
    }

    public static PlunderReward getByItem(final int itemId) {
        return rewardsByItemId.get(itemId);
    }

    public static Collection<Integer> getItemIds() {
        return rewardsByItemId.keySet();
    }

    PlunderReward(PlunderRewardTier tier, int itemId, int coins) {
        this.tier = tier;
        this.itemId = itemId;
        this.coins = coins;
    }

    public PlunderRewardTier getTier() {
        return tier;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCoins() {
        return coins;
    }
}
