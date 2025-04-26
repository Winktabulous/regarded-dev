package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OuraniaCaveBank extends OuraniaCave implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3006, 5632 },
                        { 3006, 5616 },
                        { 3028, 5616 },
                        { 3028, 5632 }
                })
        };
    }

    @Override
    public String name() {
        return "Ourania Cave (Bank)";
    }

}
