package com.zenyte.game.content.boss.cerberus.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 07/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EasternCerberusArea extends StaticCerberusLair {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1343, 1274 },
                        { 1343, 1243 },
                        { 1387, 1243 },
                        { 1387, 1274 }
                })
        };
    }

    @Override
    public String name() {
        return "Cerberus Lair (Eastern)";
    }

}
