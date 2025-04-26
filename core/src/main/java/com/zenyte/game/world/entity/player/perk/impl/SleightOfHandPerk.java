package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 12-1-2019 | 19:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SleightOfHandPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Sleight of Hand";
    }

    @Override
    public String getDescription() {
        return "Get an additional 15% chance on succeeding whilst pickpocketing.";
    }
}
