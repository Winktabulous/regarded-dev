package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 14-1-2019 | 01:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KillTheCyclopsPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Kill The Cyclops";
    }

    @Override
    public String getDescription() {
        return "Receive 5 extra warrior's guild tokens when killing animated armours";
    }
}
