package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.content.wildernessVault.reward.VaultRewardItem;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class WildernessVaultChestTable implements AlternateTableDropProvider {

    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        for(VaultRewardItem item: VaultRewardItem.REWARDS.items()) {
            if(item.getProbability() != 1)
                entries.add(new OtherDropViewerEntry(item.getId(), item.getMin(), item.getMax(), item.getProbability(), VaultRewardItem.REWARDS.getTotalWeight(), ""));
            else {
                entries.add(new OtherDropViewerEntry(item.getId(), item.getMin(), item.getMax(), 1, 1, ""));
            }
        }
    }

    @Override
    public String getName() {
        return "Chests - Wildy Vault";
    }
}
