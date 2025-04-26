package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.content.chambersofxeric.rewards.ChallengeRaidNormalReward;
import com.zenyte.game.content.chambersofxeric.rewards.RaidNormalReward;
import com.zenyte.game.content.chambersofxeric.rewards.RaidRareReward;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Raids1CMTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        int tw = ChallengeRaidNormalReward.values().length;
        /* Add Normal Rewards */
        for(ChallengeRaidNormalReward reward: ChallengeRaidNormalReward.values()) {
            entries.add(new OtherDropViewerEntry(reward.getId(), 1, reward.getMaximumAmount(), 1, tw, ""));
        }
        for(RaidRareReward reward: RaidRareReward.values()) {
            entries.add(new OtherDropViewerEntry(reward.getItem().getId(), 1,1, reward.getWeight(), RaidRareReward.TOTAL_WEIGHT * 4, "This item's rate is based on a 100,000 point raid completion and is awarded to a party member based on contribution."));
        }
        entries.add(new OtherDropViewerEntry(ItemId.OLMLET, 1, 1, 1, 65, "This pet's drop rate is decreased to 1/53 when a unique drop is rolled."));
        entries.add(new OtherDropViewerEntry(ItemId.ANCIENT_TABLET, 1, 1, 1, 10, "This item is removed from the drop table after it is rewarded to a player."));
        entries.add(new OtherDropViewerEntry(2811, 1, 1, 1, 12, ""));
        entries.add(new OtherDropViewerEntry(ItemId.METAMORPHIC_DUST, 1, 1, 1, 200, "This item is only eligible to drop under the CM target time."));
        entries.add(new OtherDropViewerEntry(ItemId.TWISTED_ANCESTRAL_COLOUR_KIT, 1, 1, 1, 40, "This item is only eligible to drop under the CM target time."));
        entries.add(new OtherDropViewerEntry(ItemId.TWISTED_HORNS, 1, 1, 1, 75, "This item is only eligible to drop under the CM target time."));


    }

    @Override
    public String getName() {
        return "Raids - CoX CM";
    }
}
