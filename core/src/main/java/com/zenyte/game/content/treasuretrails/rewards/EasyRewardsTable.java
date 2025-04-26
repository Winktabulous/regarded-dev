package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 24/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EasyRewardsTable extends ClueRewardTable {

    protected final List<ImmutableItem> commonSharedTable = tableOf(
            item(COINS_995, 50, 200),
            item(PURPLE_SWEETS_10476, 2, 6)
    );

    protected final List<ImmutableItem> uncommonSharedTable = tableOf(
            item(COINS_995, 3000, 7000)
    );

    protected final List<ImmutableItem> rareSweetsTable = tableOf(
            item(COINS_995, 10000, 15000),
            item(PURPLE_SWEETS_10476, 8, 12)
    );

    protected final List<ImmutableItem> rareCoinsTable = tableOf(
            item(COINS_995, 3400, 4800)
    );

    private final List<ImmutableItem> commonItemsTable = tableOf(
            item(BLACK_FULL_HELM),
            item(BLACK_PLATEBODY),
            item(BLACK_PLATELEGS),
            item(BLACK_LONGSWORD),
            item(BLACK_BATTLEAXE),
            item(BLACK_AXE),
            item(BLACK_DAGGER),
            item(STEEL_PICKAXE),
            item(BLACK_PICKAXE),
            item(COIF),
            item(STUDDED_BODY),
            item(STUDDED_CHAPS),
            item(WILLOW_SHORTBOW),
            item(STAFF_OF_AIR),
            item(AIR_RUNE, 30, 50),
            item(MIND_RUNE, 30, 50),
            item(WATER_RUNE, 30, 50),
            item(EARTH_RUNE, 30, 50),
            item(FIRE_RUNE, 30, 50),
            item(BODY_RUNE, 30, 50),
            item(CHAOS_RUNE, 5, 10),
            item(NATURE_RUNE, 5, 10),
            item(LAW_RUNE, 5, 10),
            notedItem(TROUT, 6, 10),
            notedItem(SALMON, 6, 10)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            item(WILLOW_LONGBOW),
            item(AMULET_OF_MAGIC)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            item(AMULET_OF_MAGIC_T),
            item(WILLOW_COMP_BOW)
    );

    private final List<ImmutableItem> rareCosmeticsTable = tableOf(
            item(WOODEN_SHIELD_G),
            item(BLACK_FULL_HELM_T),
            item(BLACK_PLATEBODY_T),
            item(BLACK_PLATELEGS_T),
            item(BLACK_PLATESKIRT_T),
            item(BLACK_KITESHIELD_T),
            item(BLACK_FULL_HELM_G),
            item(BLACK_PLATEBODY_G),
            item(BLACK_PLATELEGS_G),
            item(BLACK_PLATESKIRT_G),
            item(BLACK_KITESHIELD_G),
            item(BLACK_SHIELD_H1),
            item(BLACK_SHIELD_H2),
            item(BLACK_SHIELD_H3),
            item(BLACK_SHIELD_H4),
            item(BLACK_SHIELD_H5),
            item(BLACK_HELM_H1),
            item(BLACK_HELM_H2),
            item(BLACK_HELM_H3),
            item(BLACK_HELM_H4),
            item(BLACK_HELM_H5),
            item(BLACK_PLATEBODY_H1),
            item(BLACK_PLATEBODY_H2),
            item(BLACK_PLATEBODY_H3),
            item(BLACK_PLATEBODY_H4),
            item(BLACK_PLATEBODY_H5),
            item(STEEL_FULL_HELM_T),
            item(STEEL_PLATEBODY_T),
            item(STEEL_PLATELEGS_T),
            item(STEEL_PLATESKIRT_T),
            item(STEEL_KITESHIELD_T),
            item(STEEL_FULL_HELM_G),
            item(STEEL_PLATEBODY_G),
            item(STEEL_PLATELEGS_G),
            item(STEEL_PLATESKIRT_G),
            item(STEEL_KITESHIELD_G),
            item(IRON_FULL_HELM_T),
            item(IRON_PLATEBODY_T),
            item(IRON_PLATELEGS_T),
            item(IRON_PLATESKIRT_T),
            item(IRON_KITESHIELD_T),
            item(IRON_FULL_HELM_G),
            item(IRON_PLATEBODY_G),
            item(IRON_PLATELEGS_G),
            item(IRON_PLATESKIRT_G),
            item(IRON_KITESHIELD_G),
            item(BRONZE_FULL_HELM_T),
            item(BRONZE_PLATEBODY_T),
            item(BRONZE_PLATELEGS_T),
            item(BRONZE_PLATESKIRT_T),
            item(BRONZE_KITESHIELD_T),
            item(BRONZE_FULL_HELM_G),
            item(BRONZE_PLATEBODY_G),
            item(BRONZE_PLATELEGS_G),
            item(BRONZE_PLATESKIRT_G),
            item(BRONZE_KITESHIELD_G),
            item(STUDDED_BODY_G),
            item(STUDDED_CHAPS_G),
            item(STUDDED_BODY_T),
            item(STUDDED_CHAPS_T),
            item(LEATHER_BODY_G),
            item(LEATHER_CHAPS_G),
            item(BLUE_WIZARD_HAT_G),
            item(BLUE_WIZARD_ROBE_G),
            item(BLUE_SKIRT_G),
            item(BLUE_WIZARD_HAT_T),
            item(BLUE_WIZARD_ROBE_T),
            item(BLUE_SKIRT_T),
            item(BLACK_WIZARD_HAT_G),
            item(BLACK_WIZARD_ROBE_G),
            item(BLACK_SKIRT_G),
            item(BLACK_WIZARD_HAT_T),
            item(BLACK_WIZARD_ROBE_T),
            item(BLACK_SKIRT_T),
            item(SARADOMIN_ROBE_TOP),
            item(SARADOMIN_ROBE_LEGS),
            item(GUTHIX_ROBE_TOP),
            item(GUTHIX_ROBE_LEGS),
            item(ZAMORAK_ROBE_TOP),
            item(ZAMORAK_ROBE_LEGS),
            item(ANCIENT_ROBE_TOP),
            item(ANCIENT_ROBE_LEGS),
            item(ARMADYL_ROBE_TOP),
            item(ARMADYL_ROBE_LEGS),
            item(BANDOS_ROBE_TOP),
            item(BANDOS_ROBE_LEGS),
            item(BOBS_RED_SHIRT),
            item(BOBS_GREEN_SHIRT),
            item(BOBS_BLUE_SHIRT),
            item(BOBS_BLACK_SHIRT),
            item(BOBS_PURPLE_SHIRT),
            item(HIGHWAYMAN_MASK),
            item(BLUE_BERET),
            item(BLACK_BERET),
            item(RED_BERET),
            item(WHITE_BERET),
            item(A_POWDERED_WIG),
            item(BEANIE),
            item(IMP_MASK),
            item(GOBLIN_MASK),
            item(SLEEPING_CAP),
            item(FLARED_TROUSERS),
            item(PANTALOONS),
            item(BLACK_CANE),
            item(STAFF_OF_BOB_THE_CAT),
            item(AMULET_OF_POWER_T),
            item(HAM_JOINT),
            item(RAIN_BOW)
    );

    private final List<ImmutableItem> veryRareCosmeticsTable = tableOf(
            item(GOLDEN_CHEFS_HAT),
            item(GOLDEN_APRON),
            item(RED_ELEGANT_SHIRT),
            item(RED_ELEGANT_BLOUSE),
            item(RED_ELEGANT_LEGS),
            item(RED_ELEGANT_SKIRT),
            item(GREEN_ELEGANT_SHIRT),
            item(GREEN_ELEGANT_BLOUSE),
            item(GREEN_ELEGANT_LEGS),
            item(GREEN_ELEGANT_SKIRT),
            item(BLUE_ELEGANT_SHIRT),
            item(BLUE_ELEGANT_BLOUSE),
            item(BLUE_ELEGANT_LEGS),
            item(BLUE_ELEGANT_SKIRT)
    );

    private final List<ImmutableItem> capeCosmeticsTable = tableOf(
            item(TEAM_CAPE_ZERO),
            item(TEAM_CAPE_I),
            item(TEAM_CAPE_X),
            item(CAPE_OF_SKULLS)
    );

    private final List<ImmutableItem> monkRobesTable = tableOf(
            item(MONKS_ROBE_TOP_G),
            item(MONKS_ROBE_G)
    );

    @Override
    protected void defineTables() {
        appendTable(36, commonItemsTable);
        appendTable(40, uncommonItemsTable);
        appendTable(360, rareItemsTable);
        appendTable(1404, rareCosmeticsTable);
        appendTable(2808, veryRareCosmeticsTable);
        appendTable(5616, capeCosmeticsTable);
        appendTable(14040, monkRobesTable);

        appendTable(36, commonSharedTable);
        appendTable(54, uncommonSharedTable);
        appendTable(180, firelightersTable);
        appendTable(308, rareSweetsTable);
        appendTable(452, teleportsTable);
        appendTable(792, scrollbookTable);
        appendTable(864, godPagesTable);
        appendTable(1755, rareCoinsTable);
        appendTable(2160, blessingsTable);
    }

    @Override
    protected int minRolls() {
        return 2;
    }

    @Override
    protected int maxRolls() {
        return 4;
    }

    @Override
    protected int getMasterScrollRate() {
        return 50;
    }
}
