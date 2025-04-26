package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 12-1-2019 | 00:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AthleticRunnerPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Agile Runner";
    }

    @Override
    public String getDescription() {
        return "Replenish run energy 25% faster outside of Wilderness.";
    }
}
