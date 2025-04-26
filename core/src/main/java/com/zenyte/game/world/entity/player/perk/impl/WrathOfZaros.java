package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Kris | 27/03/2019 21:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WrathOfZaros extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Wrath of Zaros";
    }

    @Override
    public String getDescription() {
        return "Godwars chambers will require 50% less essence of the respective followers to enter.";
    }
}
