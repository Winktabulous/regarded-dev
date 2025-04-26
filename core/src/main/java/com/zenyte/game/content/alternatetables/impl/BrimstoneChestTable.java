package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.content.kebos.konar.plugins.objects.BrimstoneChest;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BrimstoneChestTable implements AlternateTableDropProvider {

    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        entries.addAll(BrimstoneChest.ChestReward.toEntries());
    }

    @Override
    public String getName() {
        return "Chests - Brimstone";
    }
}
