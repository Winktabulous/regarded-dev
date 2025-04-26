package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Rellekka extends FremennikProvince {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2560, 3776 },
                        { 2560, 3648 },
                        { 2688, 3648 },
                        { 2688, 3776 }
                })
        };
    }

    @Override
    public String name() {
        return "Rellekka";
    }
}
