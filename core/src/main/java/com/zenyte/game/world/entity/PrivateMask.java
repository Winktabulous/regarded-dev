package com.zenyte.game.world.entity;

import com.zenyte.game.world.entity.player.Player;

public class PrivateMask {

    protected Player[] viewers;

    public boolean canApply(Player player) {
        if (viewers != null) {
            for (Player p : viewers) {
                if (player.equals(p)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void setViewers(Player... players) {
        this.viewers = players;
    }
}
