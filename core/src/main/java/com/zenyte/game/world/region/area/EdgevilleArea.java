package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 26/03/2019 14:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleArea extends KingdomOfMisthalin implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {3072, 3521},
                        {3072, 3449},
                        {3099, 3449},
                        {3104, 3461},
                        {3109, 3462},
                        {3120, 3462},
                        {3123, 3465},
                        {3132, 3465},
                        {3135, 3468},
                        {3141, 3468},
                        {3138, 3472},
                        {3138, 3483},
                        {3141, 3485},
                        {3141, 3491},
                        {3138, 3494},
                        {3138, 3514},
                        {3142, 3518},
                        {3142, 3521}
                })
        };
    }

    @Override
    public String name() {
        return "Edgeville";
    }

    @Override
    public String restrictionMessage() {
        return "The Grand Exchange staff prefer not to have heavy artillery operated around their premises.";
    }
}
