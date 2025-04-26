package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;
/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HardRewardsTable extends ClueRewardTable {

    protected final List<ImmutableItem> commonSharedTable = tableOf(
            item(COINS_995, 1000, 5000),
            item(PURPLE_SWEETS_10476, 7, 15)
    );

    protected final List<ImmutableItem> rareSweetsTable = tableOf(
            item(COINS_995, 10000, 15000),
            item(PURPLE_SWEETS_10476, 8, 12)
    );

    private final List<ImmutableItem> commonItemsTable = tableOf(
            item(RUNE_FULL_HELM),
            item(RUNE_PLATEBODY),
            item(RUNE_PLATELEGS),
            item(RUNE_PLATESKIRT),
            item(RUNE_KITESHIELD),
            item(RUNE_LONGSWORD),
            item(RUNE_DAGGER),
            item(RUNE_BATTLEAXE),
            item(RUNE_AXE),
            item(RUNE_PICKAXE),
            item(BLACK_DHIDE_BODY),
            item(BLACK_DHIDE_CHAPS),
            item(MAGIC_SHORTBOW),
            item(NATURE_RUNE, 30, 50),
            item(LAW_RUNE, 30, 50),
            item(BLOOD_RUNE, 20, 30),
            notedItem(LOBSTER, 12, 15),
            notedItem(SHARK, 12, 15)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            item(MAGIC_LONGBOW)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            item(MAGIC_COMP_BOW)
    );

    private final List<ImmutableItem> rareCosmeticsTable = tableOf(
            // 1 in 1,625
            item(AMULET_OF_GLORY_T4),
            item(ROBIN_HOOD_HAT),
            item(ENCHANTED_HAT),
            item(ENCHANTED_TOP),
            item(ENCHANTED_ROBE),
            item(DRAGON_BOOTS_ORNAMENT_KIT),
            item(RUNE_FULL_HELM_T),
            item(RUNE_PLATEBODY_T),
            item(RUNE_PLATELEGS_T),
            item(RUNE_PLATESKIRT_T),
            item(RUNE_KITESHIELD_T),
            item(RUNE_FULL_HELM_G),
            item(RUNE_PLATEBODY_G),
            item(RUNE_PLATELEGS_G),
            item(RUNE_PLATESKIRT_G),
            item(RUNE_KITESHIELD_G),
            item(RUNE_SHIELD_H1),
            item(RUNE_SHIELD_H2),
            item(RUNE_SHIELD_H3),
            item(RUNE_SHIELD_H4),
            item(RUNE_SHIELD_H5),
            item(RUNE_HELM_H1),
            item(RUNE_HELM_H2),
            item(RUNE_HELM_H3),
            item(RUNE_HELM_H4),
            item(RUNE_HELM_H5),
            item(ZAMORAK_FULL_HELM),
            item(ZAMORAK_PLATEBODY),
            item(ZAMORAK_PLATELEGS),
            item(ZAMORAK_PLATESKIRT),
            item(ZAMORAK_KITESHIELD),
            item(GUTHIX_FULL_HELM),
            item(GUTHIX_PLATEBODY),
            item(GUTHIX_PLATELEGS),
            item(GUTHIX_PLATESKIRT),
            item(GUTHIX_KITESHIELD),
            item(SARADOMIN_FULL_HELM),
            item(SARADOMIN_PLATEBODY),
            item(SARADOMIN_PLATELEGS),
            item(SARADOMIN_PLATESKIRT),
            item(SARADOMIN_KITESHIELD),
            item(ANCIENT_FULL_HELM),
            item(ANCIENT_PLATEBODY),
            item(ANCIENT_PLATELEGS),
            item(ANCIENT_PLATESKIRT),
            item(ANCIENT_KITESHIELD),
            item(ARMADYL_FULL_HELM),
            item(ARMADYL_PLATEBODY),
            item(ARMADYL_PLATELEGS),
            item(ARMADYL_PLATESKIRT),
            item(ARMADYL_KITESHIELD),
            item(BANDOS_FULL_HELM),
            item(BANDOS_PLATEBODY),
            item(BANDOS_PLATELEGS),
            item(BANDOS_PLATESKIRT),
            item(BANDOS_KITESHIELD),
            item(RED_DHIDE_BODY_G),
            item(RED_DHIDE_BODY_T),
            item(RED_DHIDE_CHAPS_G),
            item(RED_DHIDE_CHAPS_T),
            item(BLUE_DHIDE_BODY_G),
            item(BLUE_DHIDE_BODY_T),
            item(BLUE_DHIDE_CHAPS_G),
            item(BLUE_DHIDE_CHAPS_T),
            item(SARADOMIN_COIF),
            item(SARADOMIN_DHIDE),
            item(SARADOMIN_CHAPS),
            item(SARADOMIN_BRACERS),
            item(SARADOMIN_DHIDE_BOOTS),
            item(GUTHIX_COIF),
            item(GUTHIX_DHIDE),
            item(GUTHIX_CHAPS),
            item(GUTHIX_BRACERS),
            item(GUTHIX_DHIDE_BOOTS),
            item(ZAMORAK_COIF),
            item(ZAMORAK_DHIDE),
            item(ZAMORAK_CHAPS),
            item(ZAMORAK_BRACERS),
            item(ZAMORAK_DHIDE_BOOTS),
            item(BANDOS_COIF),
            item(BANDOS_DHIDE),
            item(BANDOS_CHAPS),
            item(BANDOS_BRACERS),
            item(BANDOS_DHIDE_BOOTS),
            item(ARMADYL_COIF),
            item(ARMADYL_DHIDE),
            item(ARMADYL_CHAPS),
            item(ARMADYL_BRACERS),
            item(ARMADYL_DHIDE_BOOTS),
            item(ANCIENT_COIF),
            item(ANCIENT_DHIDE),
            item(ANCIENT_CHAPS),
            item(ANCIENT_BRACERS),
            item(ANCIENT_DHIDE_BOOTS),
            item(SARADOMIN_STOLE),
            item(SARADOMIN_CROZIER),
            item(GUTHIX_STOLE),
            item(GUTHIX_CROZIER),
            item(ZAMORAK_STOLE),
            item(ZAMORAK_CROZIER),
            item(ZOMBIE_HEAD_19912),
            item(CYCLOPS_HEAD),
            item(PIRATES_HAT),
            item(RED_CAVALIER),
            item(WHITE_CAVALIER),
            item(NAVY_CAVALIER),
            item(TAN_CAVALIER),
            item(DARK_CAVALIER),
            item(BLACK_CAVALIER),
            item(PITH_HELMET),
            item(EXPLORER_BACKPACK),
            item(GREEN_DRAGON_MASK),
            item(BLUE_DRAGON_MASK),
            item(RED_DRAGON_MASK),
            item(BLACK_DRAGON_MASK),
            item(NUNCHAKU),
            item(RUNE_CANE),
            item(DUAL_SAI),
            item(THIEVING_BAG),
            item(RUNE_DEFENDER_ORNAMENT_KIT),
            item(BERSERKER_NECKLACE_ORNAMENT_KIT),
            item(TZHAARKETOM_ORNAMENT_KIT)
    );

    private final List<ImmutableItem> veryRareCosmeticsTable = tableOf(
            // 1 in 8,125
            item(RUNE_PLATEBODY_H1),
            item(RUNE_PLATEBODY_H2),
            item(RUNE_PLATEBODY_H3),
            item(RUNE_PLATEBODY_H4),
            item(RUNE_PLATEBODY_H5)
    );

    private final List<ImmutableItem> veryVeryRareCosmeticsTable = tableOf(
            // 1 in 9,750
            item(SARADOMIN_DHIDE_SHIELD),
            item(GUTHIX_DHIDE_SHIELD),
            item(ZAMORAK_DHIDE_SHIELD),
            item(BANDOS_DHIDE_SHIELD),
            item(ARMADYL_DHIDE_SHIELD),
            item(ANCIENT_DHIDE_SHIELD)
    );

    private final List<ImmutableItem> potionsTable = tableOf(
            // 1 in 16,250
            notedItem(SUPER_ENERGY4, 15),
            notedItem(SUPER_RESTORE4, 15),
            notedItem(ANTIFIRE_POTION4, 15),
            //Super strength and super defence are given alongside super attack.}
            notedItem(SUPER_ATTACK4, 15)
    );

    private final List<ImmutableItem> gildedTable = tableOf(
            // 1 in 35,750
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
            // 1 in 211,250
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
            item(_3RD_AGE_AMULET)
    );

    @Override
    protected void defineTables() {
        appendTable(27, commonItemsTable);
        appendTable(30, uncommonItemsTable);
        appendTable(270, rareItemsTable);
        appendTable(1625, rareCosmeticsTable);
        appendTable(8125, veryRareCosmeticsTable);
        appendTable(9750, veryVeryRareCosmeticsTable);
        appendTable(16250, potionsTable);
        appendTable(35750, gildedTable);
        appendTable(211250, thirdAgeTable);

        appendTable(27, commonSharedTable);
        appendTable(135, firelightersTable);
        appendTable(77, rareSweetsTable);
        appendTable(340, teleportsTable);
        appendTable(595, scrollbookTable);
        appendTable(650, godPagesTable);
        appendTable(541, blessingsTable);
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
        return 15;
    }
}
