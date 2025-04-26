package com.zenyte.game.content.chambersofxeric.rewards;

import com.zenyte.game.util.Utils;

/**
 * @author Kris | 12/09/2019 19:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum RaidNormalReward implements RaidReward {

    DEATH_RUNE(560, 3640),
    BLOOD_RUNE(565, 4095),
    SOUL_RUNE(566, 6554),
    RUNE_ARROW(892, 9437),
    DRAGON_ARROW(11212, 926),
    GRIMY_TOADFLAX(3049, 354),
    GRIMY_RANARR_WEED(207, 164),
    GRIMY_IRIT_LEAF(209, 809),
    GRIMY_AVANTOE(211, 404),
    GRIMY_KWUARM(213, 338),
    GRIMY_SNAPDRAGON(3051, 134),
    GRIMY_CADANTINE(215, 394),
    GRIMY_LANTADYME(2485, 526),
    GRIMY_DWARF_WEED(217, 655),
    GRIMY_TORSTOL(219, 161),
    SILVER_ORE(442, 6553),
    COAL(453, 6553),
    GOLD_ORE(444, 2978),
    MITHRIL_ORE(447, 4095),
    ADAMANTITE_ORE(449, 789),
    RUNITE_ORE(451, 87),
    UNCUT_SAPPHIRE(1623, 693),
    UNCUT_EMERALD(1621, 923),
    UNCUT_RUBY(1619, 541),
    UNCUT_DIAMOND(1617, 255),
    LIZARDMAN_FANG(13391, 4898),
    PURE_ESSENCE(7936, 65535),
    SALTPETRE(13421, 5461),
    TEAK_PLANK(8780, 1365),
    MAHOGANY_PLANK(8782, 550),
    DYNAMITE(13573, 2427),
    TORN_PRAYER_SCROLL(21047, 17),
    DARK_RELIC(21027, 1);

    private static final RaidNormalReward[] values = values();
    private final int id;
    private final int maximumAmount;

    /**
     * Selects a random reward out of the values of this enum.
     *
     * @return a random raid reward out of the lot.
     */
    static RaidNormalReward random() {
        return values[Utils.random(values.length - 1)];
    }
    
    RaidNormalReward(int id, int maximumAmount) {
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
