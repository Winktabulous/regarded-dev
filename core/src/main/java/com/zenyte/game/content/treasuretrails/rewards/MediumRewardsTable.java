package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;
/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MediumRewardsTable extends ClueRewardTable {

    protected final List<ImmutableItem> commonSharedTable = tableOf(
            item(COINS_995, 200, 1000),
            item(PURPLE_SWEETS_10476, 5, 10)
    );

    protected final List<ImmutableItem> rareSweetsTable = tableOf(
            item(COINS_995, 10000, 15000),
            item(PURPLE_SWEETS_10476, 8, 12)
    );

    private final List<ImmutableItem> commonItemsTable = tableOf(
            item(ADAMANT_FULL_HELM),
            item(ADAMANT_PLATEBODY),
            item(ADAMANT_PLATELEGS),
            item(ADAMANT_LONGSWORD),
            item(ADAMANT_DAGGER),
            item(ADAMANT_BATTLEAXE),
            item(ADAMANT_AXE),
            item(ADAMANT_PICKAXE),
            item(GREEN_DHIDE_BODY),
            item(GREEN_DHIDE_CHAPS),
            item(YEW_SHORTBOW),
            item(FIRE_BATTLESTAFF),
            item(AIR_RUNE, 50, 100),
            item(MIND_RUNE, 50, 100),
            item(WATER_RUNE, 50, 100),
            item(EARTH_RUNE, 50, 100),
            item(FIRE_RUNE, 50, 100),
            item(CHAOS_RUNE, 10, 20),
            item(NATURE_RUNE, 10, 20),
            item(LAW_RUNE, 10, 20),
            item(DEATH_RUNE, 10, 20),
            notedItem(LOBSTER, 8, 12),
            notedItem(SWORDFISH, 8, 12)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            item(AMULET_OF_POWER),
            item(YEW_LONGBOW)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            item(YEW_COMP_BOW),
            item(STRENGTH_AMULET_T)
    );

    private final List<ImmutableItem> rareCosmeticsTable = tableOf(
            item(RANGER_BOOTS),
            item(WIZARD_BOOTS),
            item(HOLY_SANDALS),
            item(SPIKED_MANACLES),
            item(CLIMBING_BOOTS_G),
            item(ADAMANT_FULL_HELM_T),
            item(ADAMANT_PLATEBODY_T),
            item(ADAMANT_PLATELEGS_T),
            item(ADAMANT_PLATESKIRT_T),
            item(ADAMANT_KITESHIELD_T),
            item(ADAMANT_FULL_HELM_G),
            item(ADAMANT_PLATEBODY_G),
            item(ADAMANT_PLATELEGS_G),
            item(ADAMANT_PLATESKIRT_G),
            item(ADAMANT_KITESHIELD_G),
            item(ADAMANT_SHIELD_H1),
            item(ADAMANT_SHIELD_H2),
            item(ADAMANT_SHIELD_H3),
            item(ADAMANT_SHIELD_H4),
            item(ADAMANT_SHIELD_H5),
            item(ADAMANT_HELM_H1),
            item(ADAMANT_HELM_H2),
            item(ADAMANT_HELM_H3),
            item(ADAMANT_HELM_H4),
            item(ADAMANT_HELM_H5),
            item(ADAMANT_PLATEBODY_H1),
            item(ADAMANT_PLATEBODY_H2),
            item(ADAMANT_PLATEBODY_H3),
            item(ADAMANT_PLATEBODY_H4),
            item(ADAMANT_PLATEBODY_H5),
            item(MITHRIL_FULL_HELM_G),
            item(MITHRIL_PLATEBODY_G),
            item(MITHRIL_PLATELEGS_G),
            item(MITHRIL_PLATESKIRT_G),
            item(MITHRIL_KITESHIELD_G),
            item(MITHRIL_FULL_HELM_T),
            item(MITHRIL_PLATEBODY_T),
            item(MITHRIL_PLATELEGS_T),
            item(MITHRIL_PLATESKIRT_T),
            item(MITHRIL_KITESHIELD_T),
            item(GREEN_DHIDE_BODY_G),
            item(GREEN_DHIDE_BODY_T),
            item(GREEN_DHIDE_CHAPS_G),
            item(GREEN_DHIDE_CHAPS_T),
            item(SARADOMIN_MITRE),
            item(SARADOMIN_CLOAK),
            item(GUTHIX_MITRE),
            item(GUTHIX_CLOAK),
            item(ZAMORAK_MITRE),
            item(ZAMORAK_CLOAK),
            item(ANCIENT_MITRE),
            item(ANCIENT_CLOAK),
            item(ANCIENT_CROZIER),
            item(ANCIENT_STOLE),
            item(ARMADYL_MITRE),
            item(ARMADYL_CLOAK),
            item(ARMADYL_CROZIER),
            item(ARMADYL_STOLE),
            item(BANDOS_MITRE),
            item(BANDOS_CLOAK),
            item(BANDOS_CROZIER),
            item(BANDOS_STOLE),
            item(RED_BOATER),
            item(GREEN_BOATER),
            item(ORANGE_BOATER),
            item(BLACK_BOATER),
            item(BLUE_BOATER),
            item(PINK_BOATER),
            item(PURPLE_BOATER),
            item(WHITE_BOATER),
            item(RED_HEADBAND),
            item(BLACK_HEADBAND),
            item(BROWN_HEADBAND),
            item(WHITE_HEADBAND),
            item(BLUE_HEADBAND),
            item(GOLD_HEADBAND),
            item(PINK_HEADBAND),
            item(GREEN_HEADBAND),
            item(CRIER_HAT),
            item(ADAMANT_CANE),
            item(CAT_MASK),
            item(PENGUIN_MASK),
            item(LEPRECHAUN_HAT),
            item(CRIER_COAT),
            item(CRIER_BELL),
            item(ARCEUUS_BANNER),
            item(PISCARILIUS_BANNER),
            item(HOSIDIUS_BANNER),
            item(PISCARILIUS_BANNER),
            item(HOSIDIUS_BANNER),
            item(SHAYZIEN_BANNER),
            item(LOVAKENGJ_BANNER),
            item(CABBAGE_ROUND_SHIELD),
            item(WOLF_MASK),
            item(WOLF_CLOAK),
            item(BLACK_LEPRECHAUN_HAT)
    );

    private final List<ImmutableItem> veryRareCosmeticsTable = tableOf(
            item(BLACK_UNICORN_MASK),
            item(WHITE_UNICORN_MASK),
            item(PURPLE_ELEGANT_SHIRT),
            item(PURPLE_ELEGANT_BLOUSE),
            item(PURPLE_ELEGANT_LEGS),
            item(PURPLE_ELEGANT_SKIRT),
            item(BLACK_ELEGANT_SHIRT),
            item(WHITE_ELEGANT_BLOUSE),
            item(BLACK_ELEGANT_LEGS),
            item(WHITE_ELEGANT_SKIRT),
            item(PINK_ELEGANT_SHIRT),
            item(PINK_ELEGANT_BLOUSE),
            item(PINK_ELEGANT_LEGS),
            item(PINK_ELEGANT_SKIRT),
            item(GOLD_ELEGANT_SHIRT),
            item(GOLD_ELEGANT_BLOUSE),
            item(GOLD_ELEGANT_LEGS),
            item(GOLD_ELEGANT_SKIRT)
    );

    @Override
    protected void defineTables() {
        appendTable(34, commonItemsTable);
        appendTable(38, uncommonItemsTable);
        appendTable(341, rareItemsTable);
        appendTable(1133, rareCosmeticsTable);
        appendTable(2266, veryRareCosmeticsTable);

        appendTable(34, commonSharedTable);
        appendTable(189, firelightersTable);
        appendTable(97, rareSweetsTable);
        appendTable(428, teleportsTable);
        appendTable(750, scrollbookTable);
        appendTable(818, godPagesTable);
        appendTable(682, blessingsTable);
    }

    @Override
    protected int minRolls() {
        return 3;
    }

    @Override
    protected int maxRolls() {
        return 5;
    }

    @Override
    protected int getMasterScrollRate() {
        return 30;
    }
}
