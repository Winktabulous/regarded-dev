package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 12/06/2019 11:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArdougneArea extends KingdomOfKandarin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{
                { 2690, 3390 },
                { 2543, 3390 },
                { 2543, 3350 },
                { 2457, 3350 },
                { 2457, 3328 },
                { 2435, 3328 },
                { 2435, 3300 },
                { 2455, 3300 },
                { 2455, 3260 },
                { 2601, 3260 },
                { 2601, 3255 },
                { 2613, 3255 },
                { 2613, 3260 },
                { 2690, 3260 }
        }) };
    }

    @Override
    public String name() {
        return "Ardougne";
    }

}
