package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Kris | 27/03/2019 22:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Backfire extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Backfire";
    }

    @Override
    public String getDescription() {
        return "Small chance to absorb and reflect up to 75% of the base damage of a dragonfire attack from a dragon.";
    }

    @Override
    public boolean valid() {
        return Utils.random(99) < 5;
    }
}
