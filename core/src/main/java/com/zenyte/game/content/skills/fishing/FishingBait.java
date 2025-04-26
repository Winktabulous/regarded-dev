package com.zenyte.game.content.skills.fishing;

/**
 * @author Kris | 05/03/2019 15:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

public enum FishingBait {

    FISHING_BAIT(313),
    FEATHER(314),
    RAW_KARAMBWANJI(3150),
    SANDWORMS(13431),
    DARK_FISHING_BAIT(11940),
    ROE(11324),
    FISH_OFFCUTS(11334),
    CAVIAR(11326);

    private final int id;

    FishingBait(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
