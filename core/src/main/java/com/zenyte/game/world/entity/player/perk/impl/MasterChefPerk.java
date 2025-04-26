package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 14-1-2019 | 00:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MasterChefPerk extends Perk {

    private int charges = 1000;

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Master Chef";
    }

    @Override
    public String getDescription() {
        return "Get a 10% chance on cooking two portions of food whilst cooking.";
    }

    @Override
    public void consume(final Player player) {
        player.sendMessage("You cooked two portions instead of one since you have the <col=00080>" + getName() + "</col> perk!");
        charges -= 1;
    }

    @Override
    public boolean valid() {
        return true;//charges > 0;
    }
}
