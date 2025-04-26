package com.zenyte.game.content.alternatetables.impl;

import com.near_reality.game.content.elven.obj.NewCrystalChestLoot;
import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CrystalChestTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if(entries.size() == 0)
            entries.addAll(NewCrystalChestLoot.toEntries(false));
        return entries;
    }

    @Override
    public String getName() {
        return "C.Key - Regular";
    }
}
