package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Kris | 27/03/2019 21:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RiddleInTunnels extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "The riddle in the tunnels";
    }

    @Override
    public String getDescription() {
        return "Unlock all of Barrows doors and puzzles automatically.";
    }
}
