package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 12-1-2019 | 20:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LeprechaunsFriendPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Leprechaun's Friend";
    }

    @Override
    public String getDescription() {
        return "Get a 15% increased yield whilst farming.";
    }

    @Override
    public void consume(final Player player) {
        player.sendMessage("Extra yield was harvested since you have the <col=00080>" + getName() + "</col> perk!");
    }
}
