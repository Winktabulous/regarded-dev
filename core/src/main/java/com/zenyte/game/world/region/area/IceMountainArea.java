package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IceMountainArea extends KingdomOfAsgarnia implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2993, 3513 },
                        { 3009, 3512 },
                        { 3017, 3513 },
                        { 3024, 3506 },
                        { 3030, 3493 },
                        { 3028, 3484 },
                        { 3036, 3473 },
                        { 3034, 3462 },
                        { 3030, 3456 },
                        { 3019, 3439 },
                        { 2995, 3439 },
                        { 2978, 3466 },
                        { 2987, 3485 },
                        { 2991, 3498 },
                        { 2988, 3502 }
                })
        };
    }

    @Override
    public String name() {
        return "Ice Mountain";
    }

    @Override
    public String restrictionMessage() {
        return "You can't place a cannon here.";
    }
}
