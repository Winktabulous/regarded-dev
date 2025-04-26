package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;
/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EliteRewardsTable extends ClueRewardTable {

    protected final List<ImmutableItem> commonSharedTable = tableOf(
            item(COINS_995, 20000, 30000),
            item(PURPLE_SWEETS_10476, 9, 23)
    );

    protected final List<ImmutableItem> rareSweetsTable = tableOf(
            item(COINS_995, 10000, 15000),
            item(PURPLE_SWEETS_10476, 8, 12)
    );

    private final List<ImmutableItem> commonItemsTable = tableOf(
            // 1 in 32.3
            item(RUNE_PLATEBODY),
            item(RUNE_PLATELEGS),
            item(RUNE_PLATESKIRT),
            item(RUNE_KITESHIELD),
            item(RUNE_CROSSBOW),
            item(DRAGON_DAGGER),
            item(DRAGON_MACE),
            item(DRAGON_LONGSWORD),
            item(ONYX_BOLT_TIPS, 8, 12),
            item(LAW_RUNE, 50, 75),
            item(DEATH_RUNE, 50, 75),
            item(BLOOD_RUNE, 50,75),
            item(SOUL_RUNE, 50,75),
            item(DRAGONSTONE_BRACELET),
            item(DRAGON_NECKLACE),
            item(DRAGONSTONE_RING),
            notedItem(TUNA_POTATO, 15, 20),
            notedItem(SUMMER_PIE, 15, 20),
            notedItem(OAK_PLANK, 60, 80),
            notedItem(TEAK_PLANK, 40, 50),
            notedItem(MAHOGANY_PLANK, 20, 30),
            notedItem(RUNITE_BAR, 1, 3)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            // 1 in 64.6
            item(TOOTH_HALF_OF_KEY),
            item(LOOP_HALF_OF_KEY)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            // 1 in 96.9
            item(PALM_TREE_SEED),
            item(YEW_SEED),
            item(MAGIC_SEED)
    );

    private final List<ImmutableItem> rareCosmeticsTable = tableOf(
            // 1 in 1,250
            item(DRAGON_FULL_HELM_ORNAMENT_KIT),
            item(DRAGON_CHAINBODY_ORNAMENT_KIT),
            item(DRAGON_LEGSSKIRT_ORNAMENT_KIT),
            item(DRAGON_SQ_SHIELD_ORNAMENT_KIT),
            item(DRAGON_SCIMITAR_ORNAMENT_KIT),
            item(BANDOS_ORNAMENT_KIT),
            item(FURY_ORNAMENT_KIT),
            item(LIGHT_INFINITY_COLOUR_KIT),
            item(DARK_INFINITY_COLOUR_KIT),
            item(ROYAL_CROWN),
            item(ROYAL_GOWN_TOP),
            item(ROYAL_GOWN_BOTTOM),
            item(ROYAL_SCEPTRE),
            item(MUSKETEER_HAT),
            item(MUSKETEER_TABARD),
            item(MUSKETEER_PANTS),
            item(BLACK_DHIDE_BODY_G),
            item(BLACK_DHIDE_BODY_T),
            item(BLACK_DHIDE_CHAPS_G),
            item(BLACK_DHIDE_CHAPS_T),
            item(RANGERS_TUNIC),
            item(RANGER_GLOVES),
            item(HOLY_WRAPS),
            item(BRONZE_DRAGON_MASK),
            item(IRON_DRAGON_MASK),
            item(STEEL_DRAGON_MASK),
            item(MITHRIL_DRAGON_MASK),
            item(ADAMANT_DRAGON_MASK),
            item(RUNE_DRAGON_MASK),
            item(ARCEUUS_SCARF),
            item(HOSIDIUS_SCARF),
            item(LOVAKENGJ_SCARF),
            item(PISCARILIUS_SCARF),
            item(SHAYZIEN_SCARF),
            item(KATANA),
            item(DRAGON_CANE),
            item(BUCKET_HELM),
            item(BLACKSMITHS_HELM),
            item(DEERSTALKER),
            item(AFRO),
            item(BIG_PIRATE_HAT),
            item(TOP_HAT),
            item(MONOCLE),
            item(BRIEFCASE),
            item(SAGACIOUS_SPECTACLES),
            item(RANGERS_TIGHTS),
            item(URIS_HAT),
            item(GIANT_BOOT),
            item(FREMENNIK_KILT)
    );

    private final List<ImmutableItem> veryRareCosmeticsTable = tableOf(
            // 1 in 12,500
            item(DARK_BOW_TIE),
            item(DARK_TUXEDO_JACKET),
            item(DARK_TUXEDO_CUFFS),
            item(DARK_TROUSERS),
            item(DARK_TUXEDO_SHOES),
            item(LIGHT_BOW_TIE),
            item(LIGHT_TUXEDO_JACKET),
            item(LIGHT_TUXEDO_CUFFS),
            item(LIGHT_TROUSERS),
            item(LIGHT_TUXEDO_SHOES)
    );

    private final List<ImmutableItem> veryRareStuffTable = tableOf(
            // 1 in 28,750
            item(RING_OF_NATURE),
            item(CRYSTAL_KEY),
            item(LAVA_DRAGON_MASK),
            notedItem(BATTLESTAFF, 100),
            notedItem(EXTENDED_ANTIFIRE4, 30),
            notedItem(SUPER_RESTORE4, 30),
            notedItem(SARADOMIN_BREW4, 30),
            notedItem(RANGING_POTION4, 30)
    );

    private final List<ImmutableItem> gildedTable = tableOf(
            // 1 in 63,250
            item(GILDED_FULL_HELM),
            item(GILDED_PLATEBODY),
            item(GILDED_PLATELEGS),
            item(GILDED_PLATESKIRT),
            item(GILDED_KITESHIELD),
            item(GILDED_MED_HELM),
            item(GILDED_CHAINBODY),
            item(GILDED_SQ_SHIELD),
            item(GILDED_2H_SWORD),
            item(GILDED_SPEAR),
            item(GILDED_HASTA),
            item(GILDED_SCIMITAR),
            item(GILDED_BOOTS),
            item(GILDED_COIF),
            item(GILDED_DHIDE_VAMBS),
            item(GILDED_DHIDE_BODY),
            item(GILDED_DHIDE_CHAPS),
            item(GILDED_PICKAXE),
            item(GILDED_AXE),
            item(GILDED_SPADE)
    );

    private final List<ImmutableItem> thirdAgeTable = tableOf(
            // 1 in 488,750
            item(_3RD_AGE_FULL_HELMET),
            item(_3RD_AGE_PLATEBODY),
            item(_3RD_AGE_PLATELEGS),
            item(_3RD_AGE_PLATESKIRT),
            item(_3RD_AGE_KITESHIELD),
            item(_3RD_AGE_RANGE_COIF),
            item(_3RD_AGE_RANGE_TOP),
            item(_3RD_AGE_RANGE_LEGS),
            item(_3RD_AGE_VAMBRACES),
            item(_3RD_AGE_MAGE_HAT),
            item(_3RD_AGE_ROBE_TOP),
            item(_3RD_AGE_ROBE),
            item(_3RD_AGE_AMULET),
            item(_3RD_AGE_LONGSWORD),
            item(_3RD_AGE_WAND),
            item(_3RD_AGE_CLOAK),
            item(_3RD_AGE_BOW)
    );

    @Override
    protected void defineTables() {
        appendTable(32, commonItemsTable);
        appendTable(64, uncommonItemsTable);
        appendTable(97, rareItemsTable);
        appendTable(1250, rareCosmeticsTable);
        appendTable(12500, veryRareCosmeticsTable);
        appendTable(28750, veryRareStuffTable);
        appendTable(63250, gildedTable);
        appendTable(488750, thirdAgeTable);

        appendTable(32, commonSharedTable);
        appendTable(161, firelightersTable);
        appendTable(92, rareSweetsTable);
        appendTable(203, teleportsTable);
        appendTable(355, scrollbookTable);
        appendTable(775, godPagesTable);
        appendTable(645, blessingsTable);
    }

    @Override
    protected int minRolls() {
        return 4;
    }

    @Override
    protected int maxRolls() {
        return 6;
    }

    @Override
    protected int getMasterScrollRate() {
        return 5;
    }
}
