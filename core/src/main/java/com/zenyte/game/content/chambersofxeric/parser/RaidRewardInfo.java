package com.zenyte.game.content.chambersofxeric.parser;

import java.util.ArrayList;

public class RaidRewardInfo
{
    private final String playerName;
    private final ArrayList<RaidRewardItem> rewards;

    public String getPlayerName() {
        return playerName;
    }

    public ArrayList<RaidRewardItem> getRewards() {
        return rewards;
    }

    public RaidRewardInfo(String playerName, ArrayList<RaidRewardItem> rewards) {
        this.playerName = playerName;
        this.rewards = rewards;
    }
}
