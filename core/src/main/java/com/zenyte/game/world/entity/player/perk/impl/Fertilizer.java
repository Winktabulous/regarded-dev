package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Kris | 27/03/2019 22:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Fertilizer extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Fertilizer";
    }

    @Override
    public String getDescription() {
        return "All farming crops are 50% less likely to catch disease; Stacks multiplicatively with composts!";
    }
}
