package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 13/10/2019 | 19:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LizardmanCanyon extends GreatKourend {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] {
                { 1471, 3682 },
                { 1472, 3712 },
                { 1498, 3726 },
                { 1571, 3696 },
                { 1577, 3678 },
                { 1557, 3665 },
                { 1536, 3676 },
                { 1484, 3684 }
        })};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Lizardman Canyon";
    }
}
