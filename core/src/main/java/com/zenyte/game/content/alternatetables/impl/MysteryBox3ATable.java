package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.plugins.item.mysteryboxes.ThirdAgeMysteryBox;
import com.zenyte.plugins.item.mysteryboxes.UltimateMysteryBox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MysteryBox3ATable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if(entries.size() == 0)
            entries.addAll(ThirdAgeMysteryBox.toEntries());
        return entries;
    }

    @Override
    public String getName() {
        return "M.Box - 3rd Age";
    }
}
