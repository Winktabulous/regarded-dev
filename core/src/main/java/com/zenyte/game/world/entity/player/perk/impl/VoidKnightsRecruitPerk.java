package com.zenyte.game.world.entity.player.perk.impl;

import com.zenyte.game.world.entity.player.perk.Perk;

/**
 * @author Tommeh | 14-1-2019 | 00:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VoidKnightsRecruitPerk extends Perk {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Void Knight's Recruit";
    }

    @Override
    public String getDescription() {
        return "Get 20% more void knight commendation points per successful Pest control game.";
    }
}
