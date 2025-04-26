package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;
/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MasterRewardsTable extends ClueRewardTable {

    protected final List<ImmutableItem> commonSharedTable = tableOf(
            item(COINS_995, 20000, 35000),
            item(PURPLE_SWEETS_10476, 14, 33)
    );

    protected final List<ImmutableItem> rareSweetsTable = tableOf(
            item(COINS_995, 10000, 15000),
            item(PURPLE_SWEETS_10476, 8, 12)
    );

    protected final List<ImmutableItem> rareCoinsTable = tableOf(
            item(COINS_995, 15000, 30000)
    );

    private final List<ImmutableItem> commonItemsTable = tableOf(
            // 1 in 30.3
            item(DRAGON_DAGGER),
            item(DRAGON_MACE),
            item(DRAGON_LONGSWORD),
            item(DRAGON_SCIMITAR),
            item(DRAGON_BATTLEAXE),
            item(DRAGON_HALBERD),
            item(NATURE_RUNE, 100, 200),
            item(DEATH_RUNE, 100, 200),
            item(SOUL_RUNE, 100, 200),
            item(ONYX_BOLTS_E, 15, 25),
            notedItem(MANTA_RAY, 15, 25),
            notedItem(WINE_OF_ZAMORAK, 35, 50),
            notedItem(LIMPWURT_ROOT, 40, 60),
            notedItem(GRIMY_RANARR_WEED, 5, 10),
            notedItem(GRIMY_TOADFLAX, 25, 35),
            notedItem(GRIMY_SNAPDRAGON, 5, 10),
            notedItem(RUNITE_ORE, 5, 8),
            notedItem(RUNITE_BAR, 5, 7),
            notedItem(BLACK_DRAGONHIDE, 5, 25)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            // 1 in 60.6
            item(TOOTH_HALF_OF_KEY),
            item(LOOP_HALF_OF_KEY)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            // 1 in 91
            item(PALM_TREE_SEED, 1,2),
            item(YEW_SEED, 1,2),
            item(MAGIC_SEED, 1, 2)
    );

    private final List<ImmutableItem> rareCosmeticsTable = tableOf(
            // 1 in 851
            item(OCCULT_ORNAMENT_KIT),
            item(TORTURE_ORNAMENT_KIT),
            item(ANGUISH_ORNAMENT_KIT),
            item(TORMENTED_ORNAMENT_KIT),
            item(DRAGON_DEFENDER_ORNAMENT_KIT),
            item(HOOD_OF_DARKNESS),
            item(ROBE_TOP_OF_DARKNESS),
            item(ROBE_BOTTOM_OF_DARKNESS),
            item(GLOVES_OF_DARKNESS),
            item(BOOTS_OF_DARKNESS),
            item(SAMURAI_KASA),
            item(SAMURAI_SHIRT),
            item(SAMURAI_GLOVES),
            item(SAMURAI_GREAVES),
            item(SAMURAI_BOOTS),
            item(ARCEUUS_HOOD),
            item(HOSIDIUS_HOOD),
            item(LOVAKENGJ_HOOD),
            item(PISCARILIUS_HOOD),
            item(SHAYZIEN_HOOD),
            item(OLD_DEMON_MASK),
            item(LESSER_DEMON_MASK),
            item(GREATER_DEMON_MASK),
            item(BLACK_DEMON_MASK),
            item(JUNGLE_DEMON_MASK),
            item(LEFT_EYE_PATCH),
            item(BOWL_WIG),
            item(ALE_OF_THE_GODS),
            item(OBSIDIAN_CAPE_R),
            item(FANCY_TIARA),
            item(HALF_MOON_SPECTACLES)
    );

    private final List<ImmutableItem> veryRareCosmeticsTable = tableOf(
            // 1 in 3,404
            item(ARMADYL_GODSWORD_ORNAMENT_KIT),
            item(BANDOS_GODSWORD_ORNAMENT_KIT),
            item(SARADOMIN_GODSWORD_ORNAMENT_KIT),
            item(ZAMORAK_GODSWORD_ORNAMENT_KIT),
            item(DRAGON_PLATEBODY_ORNAMENT_KIT)
    );

    private final List<ImmutableItem> veryRareStuffTable = tableOf(
            // 1 in 12,765
            item(DRAGON_PLATEBODY_ORNAMENT_KIT),
            item(ANKOU_MASK),
            item(ANKOU_TOP),
            item(ANKOU_GLOVES),
            item(ANKOUS_LEGGINGS),
            item(ANKOU_SOCKS),
            item(MUMMYS_HEAD),
            item(MUMMYS_BODY),
            item(MUMMYS_HANDS),
            item(MUMMYS_LEGS),
            item(MUMMYS_FEET)
    );

    private final List<ImmutableItem> veryVeryRareStuffTable = tableOf(
            // 1 in 13,616
            item(BUCKET_HELM_G),
            item(RING_OF_COINS),
            item(CABBAGE, 3),
            notedItem(ANTIVENOM4_12913, 15),
            notedItem(TORSTOL, 50),
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

    private final List<ImmutableItem> veryVeryVeryRareStuffTable = tableOf(
            // 1 in 25,530
            item(DRAGON_KITESHIELD_ORNAMENT_KIT)

    );

    private final List<ImmutableItem> gildedTable = tableOf(
            // 1 in 149,776
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
            item(GILDED_HASTA)
    );

    private final List<ImmutableItem> thirdAgeTable = tableOf(
            // 1 in 313,168
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
            item(_3RD_AGE_DRUIDIC_ROBE_TOP),
            item(_3RD_AGE_DRUIDIC_ROBE_BOTTOMS),
            item(_3RD_AGE_DRUIDIC_CLOAK),
            item(_3RD_AGE_LONGSWORD),
            item(_3RD_AGE_BOW),
            item(_3RD_AGE_WAND),
            item(_3RD_AGE_DRUIDIC_STAFF),
            item(_3RD_AGE_CLOAK),
            item(_3RD_AGE_PICKAXE),
            item(_3RD_AGE_AXE)

    );

    @Override
    protected void defineTables() {
        appendTable(30, commonItemsTable);
        appendTable(60, uncommonItemsTable);
        appendTable(91, rareItemsTable);
        appendTable(851, rareCosmeticsTable);
        appendTable(3404, veryRareCosmeticsTable);
        appendTable(12765, veryRareStuffTable);
        appendTable(13616, veryVeryRareStuffTable);
        appendTable(25530, veryVeryVeryRareStuffTable);
        appendTable(149776, gildedTable);
        appendTable(313168, thirdAgeTable);

        appendTable(30, commonSharedTable);
        appendTable(151, firelightersTable);
        appendTable(86, rareSweetsTable);
        appendTable(190, teleportsTable);
        appendTable(381, rareCoinsTable);
        appendTable(333, scrollbookTable);
        appendTable(702, godPagesTable);
        appendTable(606, blessingsTable);
    }

    @Override
    protected int minRolls() {
        return 5;
    }

    @Override
    protected int maxRolls() {
        return 7;
    }

    @Override
    protected int getMasterScrollRate() {
        return 0;
    }
}
