package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Kris | 28/03/2019 12:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Pyromancer extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean valid() {
        return Utils.random(99) < 15;
    }

    @Override
    public String getName() {
        return "Pyromancer";
    }

    @Override
    public String getDescription() {
        return "A small chance to burn two logs in a single fire.";
    }
}
