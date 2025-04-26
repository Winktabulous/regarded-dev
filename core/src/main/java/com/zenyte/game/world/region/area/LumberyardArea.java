package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumberyardArea extends KingdomOfMisthalin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3298, 3518 },
                        { 3293, 3513 },
                        { 3293, 3504 },
                        { 3296, 3501 },
                        { 3296, 3497 },
                        { 3300, 3493 },
                        { 3300, 3492 },
                        { 3306, 3492 },
                        { 3307, 3493 },
                        { 3314, 3493 },
                        { 3326, 3505 },
                        { 3326, 3514 },
                        { 3322, 3518 }
                })
        };
    }

    @Override
    public String name() {
        return "Lumberyard";
    }
}
