package com.zenyte.game.content.chambersofxeric.rewards;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;

/**
 * @author Kris | 26/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ChallengeRaidNormalReward implements RaidReward {

    DEATH_RUNE(560, 3640),
    BLOOD_RUNE(565, 4095),
    SOUL_RUNE(566, 6554),
    DRAGON_ARROW(11212, 926),
    GRIMY_TOADFLAX(3049, 354),
    GRIMY_RANARR_WEED(207, 164),
    GRIMY_IRIT_LEAF(209, 668),
    GRIMY_AVANTOE(211, 354),
    GRIMY_KWUARM(213, 323),
    GRIMY_SNAPDRAGON(3051, 323),
    GRIMY_CADANTINE(215, 319),
    GRIMY_DWARF_WEED(217, 655),
    GRIMY_TORSTOL(219, 161),
    COAL(453, 6553),
    ADAMANTITE_ORE(449, 729),
    RUNITE_ORE(451, 87),
    MAHOGANY_PLANK(8782, 550),
    TORN_PRAYER_SCROLL(21047, 1),
    DARK_RELIC(21027, 1),

    ZAMORAKS_GRAPES(ItemId.ZAMORAKS_GRAPES, 500),
    WINE_OF_ZAMORAK(ItemId.WINE_OF_ZAMORAK, 450),
    RED_SPIDERS_EGGS(ItemId.RED_SPIDERS_EGGS, 1254),
    CRUSHED_SUPERIOR_DRAGON_BONES(ItemId.CRUSHED_SUPERIOR_DRAGON_BONES, 100),
    BATTLESTAVES(ItemId.BATTLESTAFF, 95),
    SNAKE_WEED(ItemId.GRIMY_SNAKE_WEED, 280),
    NAIL_BEAST_NAILS(ItemId.NAIL_BEAST_NAILS, 170),
    PURPLE_SWEETS(ItemId.PURPLE_SWEETS_10476, 350),
    STEEL_BARS(ItemId.STEEL_BAR, 850),
    UNICORN_HORN_DUST(ItemId.UNICORN_HORN_DUST, 1500),
    LAVA_SCALE_SHARD(ItemId.LAVA_SCALE_SHARD, 750);

    private static final ChallengeRaidNormalReward[] values = values();
    private final int id;
    private final int maximumAmount;

    /**
     * Selects a random reward out of the values of this enum.
     *
     * @return a random raid reward out of the lot.
     */

    public static ChallengeRaidNormalReward random() {
        return values[Utils.random(values.length - 1)];
    }

    ChallengeRaidNormalReward(int id, int maximumAmount) {
        this.id = id;
        this.maximumAmount = maximumAmount;
    }

    public int getId() {
        return id;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

}

