package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 12-1-2019 | 19:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FillTheBankPerk extends Perk { //TODO

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Fill The Bank";
    }

    @Override
    public String getDescription() {
        return "Receive an additional 200 bank space.";
    }
}
