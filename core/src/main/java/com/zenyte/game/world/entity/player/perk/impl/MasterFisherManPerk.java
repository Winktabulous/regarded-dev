package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 12-1-2019 | 16:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MasterFisherManPerk extends Perk {

    private int charges = 1000;

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Master Fisher";
    }

    @Override
    public String getDescription() {
        return "Get a 15% chance on catching double fish whilst fishing.";
    }

    @Override
    public boolean valid() {
        return true;//charges > 0;
    }

    @Override
    public void consume(final Player player) {
        player.sendFilteredMessage("You caught double fish since you have the <col=00080>" + getName() + "</col> perk!");
        charges -= 1;
    }
}
