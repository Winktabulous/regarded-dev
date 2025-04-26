package com.zenyte.game.content.alternatetables.impl;

import com.zenyte.game.content.alternatetables.AlternateTableDropProvider;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Raids2NormalTable implements AlternateTableDropProvider {

    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    double multiplier = 1.4D;

    private void buildEntries() {
        gen(ItemId.VIAL_OF_BLOOD_22446, 50, 60, 2, 30);
        gen(ItemId.DEATH_RUNE, 500, 600, 1, 30);
        gen(ItemId.BLOOD_RUNE, 500, 600, 1, 30);
        gen(ItemId.SWAMP_TAR, 500, 600, 1, 30);
        gen(ItemId.COAL, 500, 600, 1, 30);
        gen(ItemId.GOLD_ORE, 300, 360, 1, 30);
        gen(ItemId.MOLTEN_GLASS, 200, 240, 1, 30);
        gen(ItemId.ADAMANTITE_ORE, 130, 156, 1, 30);
        gen(ItemId.RUNITE_ORE, 60, 72, 1, 30);
        gen(ItemId.WINE_OF_ZAMORAK, 50, 60, 1, 30);
        gen(ItemId.POTATO_CACTUS, 50, 60, 1, 30);
        gen(ItemId.GRIMY_CADANTINE, 50, 60, 1, 30);
        gen(ItemId.GRIMY_AVANTOE, 40, 48, 1, 30);
        gen(ItemId.GRIMY_TOADFLAX, 37, 44, 1, 30);
        gen(ItemId.GRIMY_KWUARM, 36, 43, 1, 30);
        gen(ItemId.GRIMY_IRIT_LEAF, 34, 40, 1, 30);
        gen(ItemId.GRIMY_RANARR_WEED, 30, 36, 1, 30);
        gen(ItemId.GRIMY_SNAPDRAGON, 27, 32, 1, 30);
        gen(ItemId.GRIMY_LANTADYME, 26, 31, 1, 30);
        gen(ItemId.GRIMY_DWARF_WEED, 24, 28, 1, 30);
        gen(ItemId.GRIMY_TORSTOL, 20, 24, 1, 30);
        gen(ItemId.BATTLESTAFF, 15, 18, 1, 30);
        gen(ItemId.RUNE_BATTLEAXE, 4, 4, 1, 30);
        gen(ItemId.RUNE_PLATEBODY, 4, 4, 1, 30);
        gen(ItemId.RUNE_CHAINBODY, 4, 4, 1, 30);
        gen(ItemId.PALM_TREE_SEED, 3, 3, 1, 30);
        gen(ItemId.YEW_SEED, 3, 3, 1, 30);
        gen(ItemId.MAGIC_SEED, 3, 3, 1, 30);
        gen(ItemId.MAHOGANY_SEED, 10, 12, 1, 30);
        genUnique(ItemId.AVERNIC_DEFENDER_HILT, 1, 1, 4, 15);
        genUnique(ItemId.GHRAZI_RAPIER, 1, 1, 2, 15);
        genUnique(ItemId.SANGUINESTI_STAFF_UNCHARGED, 1, 1, 2, 15);
        genUnique(ItemId.JUSTICIAR_FACEGUARD, 1, 1, 2, 15);
        genUnique(ItemId.JUSTICIAR_CHESTGUARD, 1, 1, 2, 15);
        genUnique(ItemId.JUSTICIAR_LEGGUARDS, 1, 1, 2, 15);
        genUnique(ItemId.SCYTHE_OF_VITUR_UNCHARGED, 1, 1, 1, 15);

        entries.add(new OtherDropViewerEntry(ItemId.SCROLL_BOX_ELITE, 1, 1, 1, 6, ""));
        entries.add(new OtherDropViewerEntry(ItemId.LIL_ZIK, 1, 1, 1, 550, ""));
    }

    @Override
    public String getName() {
        return "Raids - ToB";
    }

    private void gen(int id, int min, int max, int top, int bottom) {
        entries.add(new OtherDropViewerEntry(id, (int) Math.max(Math.round(min * multiplier), 1), (int) Math.max(1, Math.round(max * multiplier)), top, bottom, ""));
    }

    private void genUnique(int id, int min, int max, int top, int bottom) {
        entries.add(new OtherDropViewerEntry(id, min, max, top, bottom / 0.23, "This item's rate assumes no deaths and maximum participation points by all party members in each encounter."));
    }
}
