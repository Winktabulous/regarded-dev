package com.zenyte.game.content.rewards;

/**
 * This will dictate how the reward percentage will be calculated
 */
public enum RewardPercentageStackType {

    /**
     * This will stack all entries up and change the final percentage for example
     * if we have a reward set that has a 10% chance of rolling and we have
     * 2 rewards, the final chance will be 10%
     */
    STACKED,

    /**
     * Never change the percentage
     */
    NEVER_CHANGE

}