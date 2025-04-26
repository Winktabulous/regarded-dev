package com.zenyte.game.content.bountyhunter;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26/03/2019 20:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BountyHunterVar {
    CURRENT_ROGUE_KILLS("bounty hunter rogue kills"),
    CURRENT_HUNTER_KILLS("bounty hunter hunter kills"),
    ROGUE_KILLS_RECORD("bounty hunter rogue record"),
    HUNTER_KILLS_RECORD("bounty hunter hunter record");
    @NotNull
    final String varName;

    BountyHunterVar(String varName) {
        this.varName = varName;
    }
}
