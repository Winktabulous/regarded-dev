package com.zenyte.game.content.alternatetables;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.alternatetables.impl.*;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.ClassInitializer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.List;

public enum AlternateTable {
    CLUES_BEGINNER(ClueScrollBeginnerTable.class),
    CLUES_EASY(ClueScrollEasyTable.class),
    CLUES_MEDIUM(ClueScrollMediumTable.class),
    CLUES_HARD(ClueScrollHardTable.class),
    CLUES_ELITE(ClueScrollEliteTable.class),
    CLUES_MASTER(ClueScrollMasterTable.class),
    MYSTERY_BOX_STANDARD(MysteryBoxStandardTable.class),
    MYSTERY_BOX_ULTIMATE(MysteryBoxUltimateTable.class),
    MYSTERY_BOX_3RD_AGE(MysteryBox3ATable.class),
    MYSTERY_BOX_COSMETIC(MysteryBoxCosmeticTable.class),
    MYSTERY_BOX_PVM(MysteryBoxPvmTable.class),
    MYSTERY_BOX_REGAL(MysteryBoxRegalTable.class),
    CRYSTAL_CHEST_NORMAL(CrystalChestTable.class),
    CRYSTAL_CHEST_ENHANCED(CrystalChestEnhancedTable.class),
    LARRANS_SMALL(LarransSmallChestTable.class),
    LARRANS_LARGE(LarransLargeChestTable.class),
    BRIMSTONE(BrimstoneChestTable.class),
    WILDY_VAULT(WildernessVaultChestTable.class),
    COX_NORMAL(Raids1NormalTable.class),
    COX_CM(Raids1CMTable.class),
    //TOB_ENTRY(Raids2EntryTable.class),
    TOB_NORMAL(Raids2NormalTable.class),
    TOB_HARD(Raids2HardTable.class),
    TOA_ENTRY(Raids3EntryTable.class),
    TOA_NORMAL(Raids3NormalTable.class),
    TOA_HARD(Raids3HardTable.class)

    ;

    final ObjectArrayList<DropViewerEntry> drops = new ObjectArrayList<>();
    final Object lock = new Object();
    final Class<?> clazz;
    String displayName;
    AlternateTable(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Subscribe
    public static void onPostLaunch(ServerLaunchEvent event) {
        for(AlternateTable table: values()) {
            table.load();
        }
    }

    private void load() {
        if(Arrays.stream(clazz.getInterfaces()).anyMatch(it -> it == AlternateTableDropProvider.class)) {
            try {
                ClassInitializer.initialize(clazz);
                synchronized (lock) {
                    AlternateTableDropProvider provider = (AlternateTableDropProvider) clazz.getDeclaredConstructor().newInstance();
                    for (DropViewerEntry entry : provider.getEntries())
                        drops.add(entry);
                    displayName = provider.getName();
                }
            } catch(Exception ignored) {

            }
        } else {
            throw new RuntimeException(clazz.getSimpleName() + " does not implement AlternateTableDropProvider");
        }
    }

    public List<DropViewerEntry> entries() {
        return drops;
    }

    public String getDisplayName() {
        return displayName;
    }
}
