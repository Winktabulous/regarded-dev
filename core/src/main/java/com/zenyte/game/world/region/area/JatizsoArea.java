package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 22. sept 2018 : 22:21:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class JatizsoArea extends FremennikIsles {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2369, 3808 }, { 2369, 3777 }, { 2430, 3777 }, { 2430, 3839 }, { 2414, 3839 } }) };
    }


    @Override
    public String name() {
        return "Jatizso";
    }

}
