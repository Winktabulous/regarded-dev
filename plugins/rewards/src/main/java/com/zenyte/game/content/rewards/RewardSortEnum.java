package com.zenyte.game.content.rewards;

import java.util.Comparator;

public class RewardSortEnum implements Comparator<RewardEntrySet> {
    @Override
    public int compare(RewardEntrySet o1, RewardEntrySet o2) {
        RewardSetRollType o1Type = o1.getType();
        RewardSetRollType o2Type = o2.getType();

        if(o1Type == o2Type) {
            return 0;
        }

        return o1Type == RewardSetRollType.ALWAYS_ROLL ? 1 : -1;
    }
}
