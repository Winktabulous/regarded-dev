package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 09/01/2019 18:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BlackKnightsFortress extends IceMountainArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{{3003, 3520}, {3003, 3498}, {3038, 3498}, {3038, 3520}})
        };
    }

    @Override
    public String restrictionMessage() {
        return "It is not permitted to set up a cannon this close to the Dwarf Black Guard.";
    }

    @Override
    public String name() {
        return "Black Knights' Fortress";
    }
}
