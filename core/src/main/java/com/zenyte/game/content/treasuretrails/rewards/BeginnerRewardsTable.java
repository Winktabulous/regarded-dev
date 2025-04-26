package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.model.item.ImmutableItem;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BeginnerRewardsTable extends ClueRewardTable {

    private final List<ImmutableItem> commonItemsTable = tableOf(
            // 1 in 24
            notedItem(CABBAGE, 5, 9)
    );

    private final List<ImmutableItem> uncommonItemsTable = tableOf(
            // 1 in 44.73
            item(SHORTBOW),
            item(LONGBOW),
            item(OAK_SHORTBOW),
            item(OAK_LONGBOW),
            item(IRON_PICKAXE),
            item(STAFF_OF_AIR),
            item(STAFF_OF_WATER),
            item(STAFF_OF_EARTH),
            item(STAFF_OF_FIRE),
            item(STEEL_FULL_HELM),
            item(STEEL_PLATEBODY),
            item(STEEL_PLATELEGS),
            item(STEEL_PLATESKIRT),
            item(STEEL_LONGSWORD),
            item(STEEL_DAGGER),
            item(STEEL_AXE),
            item(STEEL_BATTLEAXE),
            item(LEATHER_COWL),
            item(LEATHER_BODY),
            item(LEATHER_CHAPS),
            item(LEATHER_VAMBRACES),
            item(HARDLEATHER_BODY),
            item(BLUE_WIZARD_HAT),
            item(BLUE_WIZARD_ROBE),
            item(WIZARD_HAT),
            item(BLACK_ROBE),
            item(AIR_RUNE, 15, 35),
            item(MIND_RUNE, 15, 35),
            item(WATER_RUNE, 15, 35),
            item(EARTH_RUNE, 15, 35),
            item(FIRE_RUNE, 15, 35),
            item(BODY_RUNE, 15, 35),
            item(CHAOS_RUNE, 2, 7),
            item(NATURE_RUNE, 2, 9),
            item(LAW_RUNE, 2, 7),
            item(BRONZE_ARROW, 10, 25),
            item(IRON_ARROW, 8, 25),
            notedItem(SHRIMPS, 5, 14),
            notedItem(SARDINE, 5, 17),
            notedItem(HERRING, 5, 9)
    );

    private final List<ImmutableItem> rareItemsTable = tableOf(
            // 1 in 360
            item(MOLE_SLIPPERS),
            item(FROG_SLIPPERS),
            item(BEAR_FEET),
            item(DEMON_FEET),
            item(JESTER_CAPE),
            item(SHOULDER_PARROT),
            item(MONKS_ROBE_TOP_T),
            item(MONKS_ROBE_T),
            item(AMULET_OF_DEFENCE_T),
            item(SANDWICH_LADY_HAT),
            item(SANDWICH_LADY_TOP),
            item(SANDWICH_LADY_BOTTOM),
            item(RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX),
            item(RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN),
            item(RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK)
    );

    private final List<ImmutableItem> blackTable = tableOf(
            // 1 in 805.1
            item(BLACK_2H_SWORD),
            item(BLACK_AXE),
            item(BLACK_BATTLEAXE),
            item(BLACK_CHAINBODY),
            item(BLACK_DAGGER),
            item(BLACK_FULL_HELM),
            item(BLACK_KITESHIELD),
            item(BLACK_LONGSWORD),
            item(BLACK_MACE),
            item(BLACK_MED_HELM),
            item(BLACK_PICKAXE),
            item(BLACK_PLATEBODY),
            item(BLACK_PLATESKIRT),
            item(BLACK_PLATELEGS),
            item(BLACK_SQ_SHIELD),
            item(BLACK_SCIMITAR),
            item(BLACK_SWORD),
            item(BLACK_WARHAMMER)
    );

    @Override
    protected void defineTables() {
        appendTable(24, commonItemsTable);
        appendTable(44, uncommonItemsTable);
        appendTable(360, rareItemsTable);
        appendTable(805, blackTable);
    }

    @Override
    protected int minRolls() {
        return 1;
    }

    @Override
    protected int maxRolls() {
        return 3;
    }

    @Override
    protected int getMasterScrollRate() {
        return 0;
    }
}
