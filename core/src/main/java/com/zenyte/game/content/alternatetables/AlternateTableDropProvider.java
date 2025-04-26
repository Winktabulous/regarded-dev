package com.zenyte.game.content.alternatetables;

import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface AlternateTableDropProvider {
    ObjectArrayList<DropViewerEntry> getEntries();
    String getName();
}
