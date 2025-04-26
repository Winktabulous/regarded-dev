package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 14-1-2019 | 00:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VannakasFriendPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Vannaka's Friend";
    }

    @Override
    public String getDescription() {
        return "Get a 15% chance on choosing between 2 tasks when getting a task from any slayer master";
    }
}
