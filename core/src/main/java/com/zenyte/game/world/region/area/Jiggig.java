package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Jiggig extends FeldipHills implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2456, 3058 },
                        { 2452, 3050 },
                        { 2452, 3043 },
                        { 2464, 3037 },
                        { 2491, 3035 },
                        { 2494, 3043 },
                        { 2491, 3059 }
                })
        };
    }

    @Override
    public String name() {
        return "Jiggig";
    }
}
