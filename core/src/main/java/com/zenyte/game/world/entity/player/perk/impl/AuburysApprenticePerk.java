package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 14-1-2019 | 00:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AuburysApprenticePerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Aubury's Apprentice";
    }

    @Override
    public String getDescription() {
        return "Get a 10% chance on saving runes whilst using magic.";
    }

    @Override
    public void consume(final Player player) {
        player.sendMessage("You saved some runes on your last spell since you have the <col=00080>" + getName() + "</col> perk!");
    }
}
