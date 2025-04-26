package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 14/05/2019 23:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MiningGuildArea extends DwarvenMineArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3008, 9757 },
                        { 3008, 9664 },
                        { 3072, 9664 },
                        { 3072, 9757 }
                })
        };
    }

    @Override
    public String name() {
        return "Mining Guild";
    }
}
