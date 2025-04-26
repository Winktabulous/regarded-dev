package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Entrana extends KingdomOfKandarin implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2801, 3392 },
                        { 2801, 3328 },
                        { 2880, 3328 },
                        { 2880, 3392 },
                        { 2847, 3392 },
                        { 2847, 3394 },
                        { 2836, 3394 },
                        { 2836, 3392 }
                })
        };
    }

    @Override
    public String name() {
        return "Entrana";
    }
}
