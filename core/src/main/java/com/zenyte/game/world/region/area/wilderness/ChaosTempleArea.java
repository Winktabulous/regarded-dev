package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 08/05/2019 02:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChaosTempleArea extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3241, 3631 },
                        { 3231, 3631 },
                        { 3218, 3615 },
                        { 3224, 3602 },
                        { 3234, 3593 },
                        { 3241, 3594 },
                        { 3249, 3591 },
                        { 3253, 3604 },
                        { 3258, 3612 }
                })
        };
    }


    @Override
    public String name() {
        return "Wilderness: Chaos Temple";
    }
}
