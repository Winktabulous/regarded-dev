package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Raids3NormalTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    int points = 32000;

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        genCommon(ItemId.COINS_995, 1);
        genCommon(ItemId.DEATH_RUNE, 20);
        genCommon(ItemId.SOUL_RUNE, 40);
        genCommon(ItemId.GOLD_ORE, 90);
        genCommon(ItemId.DRAGON_DART_TIP, 100);
        genCommon(ItemId.MAHOGANY_LOGS, 100);
        genCommon(ItemId.SAPPHIRE, 200);
        genCommon(ItemId.EMERALD, 250);
        genCommon(ItemId.GOLD_BAR, 250);
        genCommon(ItemId.POTATO_CACTUS, 250);
        genCommon(ItemId.RAW_SHARK, 250);
        genCommon(ItemId.RUBY, 300);
        genCommon(ItemId.DIAMOND, 400);
        genCommon(ItemId.RAW_MANTA_RAY, 450);
        genCommon(ItemId.CACTUS_SPINE, 600);
        genCommon(ItemId.DRAGONSTONE, 600);
        genCommon(ItemId.BATTLESTAFF, 1100);
        genCommon(ItemId.COCONUT_MILK, 1100);
        genCommon(ItemId.LILY_OF_THE_SANDS, 1100);
        genCommon(ItemId.TOADFLAX_SEED, 1400);
        genCommon(ItemId.RANARR_SEED, 1800);
        genCommon(ItemId.TORSTOL_SEED, 2200);
        genCommon(ItemId.SNAPDRAGON_SEED, 2200);
        genCommon(ItemId.DRAGON_MED_HELM, 4000);
        genCommon(ItemId.MAGIC_SEED, 6500);
        genCommon(ItemId.BLOOD_ESSENCE, 7500);

        genUnique(ItemId.OSMUMTENS_FANG, 97, 2500);
        genUnique(ItemId.LIGHTBEARER, 97, 2500);
        genUnique(ItemId.ELIDINIS_WARD, 291, 25000);
        genUnique(ItemId.MASORI_MASK, 97, 12500);
        genUnique(ItemId.MASORI_BODY, 97, 12500);
        genUnique(ItemId.MASORI_CHAPS, 97, 12500);
        genUnique(ItemId.TUMEKENS_SHADOW_UNCHARGED, 97, 25000);

        genUnique(ItemId.THREAD_OF_ELIDINIS, 1, 15);
        genUnique(ItemId.BREACH_OF_THE_SCARAB, 1, 60);
        genUnique(ItemId.JEWEL_OF_THE_SUN, 1, 60);
        genUnique(ItemId.EYE_OF_THE_CORRUPTOR, 1, 60);
        genUnique(ItemId.CLUE_SCROLL_ELITE, 1, 25);
    }

    @Override
    public String getName() {
        return "Raids - ToA Normal";
    }

    private void genCommon(int id, int divisor) {
        entries.add(new OtherDropViewerEntry(id, (int) (double) (points / divisor), (int) (double) (points / divisor), 1, 26, "This item's rate reflects a lv 250 raid ending with 32,000 points."));
    }

    private void genUnique(int id, int top, int bottom) {
        entries.add(new OtherDropViewerEntry(id, 1, 1, top, bottom, "This item's rate reflects a lv 250 raid ending with 32,000 points."));
    }
}
