package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.content.treasuretrails.rewards.ClueReward;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * This adapter interfaces with the actual clue scroll rewards and caches them into a provider to be used
 * by the drop viewer interface
 *
 * @author John J. Woloszyk / Kryeus
 */
public class ClueScrollMediumTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if(entries.size() == 0)
            entries.addAll(ClueReward.getMediumTable().toEntries());
        return entries;
    }

    @Override
    public String getName() {
        return "Clue Scroll - Medium";
    }
}
