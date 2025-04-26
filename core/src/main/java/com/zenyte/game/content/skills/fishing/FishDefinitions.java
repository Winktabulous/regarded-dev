package com.zenyte.game.content.skills.fishing;

import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 04/03/2019 22:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FishDefinitions {

    SEA_SLUG(1466, 1, 10),
    KARAMTHULU(6716, 1, 10),
    SHRIMPS(317, 1, 10),
    SARDINE(327, 5, 20),
    KARAMBWANJI(3150, 5, 5),
    HERRING(345, 10, 30),
    ANCHOVIES(321, 15, 40),
    MACKERAL(353, 16, 20),
    TROUT(335, 20, 50),
    COD(341, 23, 45),
    PIKE(349, 25, 60),
    SLIMY_EEL(3379, 28, 65),
    SALMON(331, 30, 70),
    FROG_SPAWN(5004, 33, 75),
    TUNA(359, 35, 80),
    RAINBOW_FISH(10138, 38, 80),
    CAVE_EEL(5001, 38, 80),
    LOBSTER(377, 40, 90),
    BASS(363, 46, 100),
    LEAPING_TROUT(11328, 48, 50, 15, 5),
    SWORDFISH(371, 50, 100),
    LAVA_EEL(2148, 53, 30),
    INFERNAL_EEL(21293, 80, 95),
    LEAPING_SALMON(11330, 58, 70, 30, 6),
    MONKFISH(7944, 62, 120),
    KARAMBWAN(3142, 65, 50),
    LEAPING_STURGEON(11332, 70, 80, 45, 7),
    SHARK(383, 76, 110),
    SEA_TURTLE(395, 79, 38),
    MANTA_RAY(389, 81, 46),
    ANGLERFISH(13439, 82, 123),
    MINNOW(21356, 82, 26.1),
    DARK_CRAB(11934, 85, 130),
    SACRED_EEL(13339, 87, 107);

    private final int id, barbarianLevel, level;
    private final double xp, barbarianXp;
    private final String name;

    FishDefinitions(final int id, final int level, final double xp) {
        this(id, level, xp, 0, 0);
    }

    FishDefinitions(final int id, final int level, final double xp, final int barbarianLevel,
                    final double barbarianXp) {
        this.id = id;
        this.level = level;
        this.xp = xp;
        this.barbarianLevel = barbarianLevel;
        this.barbarianXp = barbarianXp;
        this.name = ItemDefinitions.getOrThrow(id).getName().toLowerCase().replace("raw ", "");
    }

    public int getId() {
        return id;
    }

    public int getBarbarianLevel() {
        return barbarianLevel;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public double getBarbarianXp() {
        return barbarianXp;
    }

    public String getName() {
        return name;
    }

}
