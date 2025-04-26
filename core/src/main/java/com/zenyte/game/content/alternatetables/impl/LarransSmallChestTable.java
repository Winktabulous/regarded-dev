package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class LarransSmallChestTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        gen(22877, 1, 3, 1, 60);
        gen(5300, 2, 7, 1, 60);
        gen(22869, 1, 5, 1, 60);
        gen(5316, 1, 3, 1, 60);
        gen(5304, 2, 5, 1, 60);
        gen(5289, 1, 5, 1, 60);
        gen(22871, 1, 2, 1, 60);
        gen(5295, 2, 5, 1, 60);

        gen(11232, 31, 149, 2, 60);
        gen(2354, 75, 200, 2, 60);
        gen(1514, 80, 120, 2, 60);
        gen( 452, 5, 10, 2, 60);

        gen(995, 40534, 114792, 3, 60);
        gen(1128, 1, 2, 3, 60);
        gen(1080, 1, 2, 3, 60);
        gen(1164, 1, 3, 3, 60);
        gen(441, 125, 300, 3, 60);
        gen(7937, 1500, 2500, 3, 60);
        gen(390, 71, 160, 3, 60);

        gen(11237, 41, 128, 4, 60);
        gen(445, 60, 90, 4, 60);

        gen(454, 282, 480, 5, 60);
        gen(1618, 15, 25, 5, 60);
        gen(1620, 20, 30, 5, 60);

    }

    private void gen(int id, int min, int max, int top, int bottom) {
        entries.add(new OtherDropViewerEntry(id, min, max, top, bottom, ""));
    }

    @Override
    public String getName() {
        return "Chests - Larran's Sm";
    }
}
