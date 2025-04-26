package com.zenyte.game.content.supplycaches;

import com.zenyte.game.util.Utils;

import java.util.Optional;

/**
 * @author Kris | 03/05/2019 16:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

public enum SupplyCache {

    //Potions
    SUPER_COMBAT_POTION(12696, 5, 25),
    PRAYER_POTION(2435, 10, 30),
    SUPER_RESTORE(3025, 10, 30),
    SARADOMIN_BREW(6686, 10, 30),
    SUPER_ANTIFIRE(21978, 5, 15),
    STAMINA(12626, 5, 30),
    ANTI_VENOM_PLUS(12914, 5, 15),
    SANFEW_SERUM(10926, 5, 30),
    //Ranging weapons
    GREY_CHINCHOMPA(10033, 50, 200),
    RED_CHINCHOMPA(10034, 50, 200),
    BLACK_CHINCHOMPA(11959, 50, 200),
    //Ammunition
    RUBY_BOLTS(9242, 100, 400),
    DIAMOND_BOLTS(9243, 100, 400),
    DRAGONSTONE_BOLTS(9244, 100, 400),
    //Herblore secondaries
    EYE_OF_NEWT(222, 25, 100),
    LIMPWURT_ROOT(226, 25, 100),
    SWAMP_TAR(1939, 25, 100),
    SNAPE_GRASS(232, 25, 100),
    CHOCOLATE_DUST(1976, 25, 100),
    JANGERBERRIES(248, 25, 100),
    MORT_MYRE_FUNGUS(2971, 25, 100),
    POTATO_CACTUS(3139, 25, 100),
    RED_SPIDERS_EGGS(224, 25, 100),
    WHITE_BERRIES(240, 25, 100),
    DRAGON_SCALE_DUST(242, 25, 100),
    GOAT_HORN_DUST(9737, 25, 100),
    UNICORN_HORN_DUST(236, 25, 100),
    //Miscellaneous
    CRYSTAL_KEYS(990, 5, 15),
    //Food
    SHARK(386, 100, 300),
    SEA_TURTLE(398, 100, 300),
    ANGLERFISH(13442, 100, 300),
    DARK_CRAB(11937, 100, 300),
    MANTA_RAY(392, 100, 300),
    TUNA_POTATO(7061, 100, 300),
    //Uncut gems
    OPAL(1626, 10, 75),
    JADE(1628, 10, 70),
    RED_TOPAZ(1630, 10, 50),
    SAPPHIRE(1624, 10, 40),
    EMERALD(1622, 10, 30),
    RUBY(1620, 10, 20),
    DIAMOND(1618, 5, 10),
    DRAGONSTONE(1632, 1, 3);

    private final int id;
    private final int min, max;

    private static final SupplyCache[] values = values();

    SupplyCache(int id, int min, int max) {
        this.id = id;
        this.min = min;
        this.max = max;
    }

    /**
     * Picks a random element from the supply caches list.
     *
     * @return the random element from the supply caches list.
     */
    public static final Optional<SupplyCache> random() {
        return Optional.ofNullable(Utils.getRandomElement(values));
    }

    public int getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
