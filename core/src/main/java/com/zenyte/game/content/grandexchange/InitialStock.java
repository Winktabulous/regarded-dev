package com.zenyte.game.content.grandexchange;

/**
 * @author Kris | 16/01/2019 01:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum InitialStock {

    AIR_RUNE(556, 1_000_000),
    WATER_RUNE(555, 1_000_000),
    EARTH_RUNE(557, 1_000_000),
    FIRE_RUNE(554, 1_000_000),
    MIND_RUNE(558, 250_000),
    BODY_RUNE(559, 250_000),
    CHAOS_RUNE(562, 100_000),
    NATURE_RUNE(561, 20_000),
    DEATH_RUNE(560, 100_000),
    BLOOD_RUNE(565, 100_000),
    SOUL_RUNE(566, 10_000),
    LAW_RUNE(563, 20_000),
    ASTRAL_RUNE(9075, 10_000),

    BRONZE_ARROW(882, 100_000),
    IRON_ARROW(884, 90_000),
    STEEL_ARROW(886, 80_000),
    MITHRIL_ARROW(888, 70_000),
    ADAMANT_ARROW(890, 60_000),
    RUNE_ARROW(892, 50_000),

    BRONZE_BOLT(877, 50_000),
    IRON_BOLT(9140, 45_000),
    STEEL_BOLT(9141, 40_000),
    MITHRIL_BOLT(9142, 30_000),
    ADAMANT_BOLT(9143, 20_000),
    RUNITE_BOLT(9144, 10_000),
    BONE_BOLT(8882, 25_000),

    HAMMER(2347, 1_000),
    KNIFE(946, 1_000),
    CHISEL(1755, 1_000),
    TINDERBOX(590, 1_000),
    SMALL_FISHING_NET(303, 1_000),
    LARGE_FISHING_NET(305, 1_000),
    FISHING_ROD(307, 1_000),
    FLY_FISHING_ROD(309, 1_000),
    LOBSTER_POT(301, 1_000),
    HARPOON(311, 1_000),
    SPADE(952, 1_000),

    ;

    private final int id, amount;

    InitialStock(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

}
