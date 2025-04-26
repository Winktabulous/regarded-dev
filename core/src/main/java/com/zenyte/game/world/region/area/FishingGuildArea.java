package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 14/05/2019 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FishingGuildArea extends KingdomOfKandarin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2595, 3425 },
                        { 2594, 3424 },
                        { 2592, 3424 },
                        { 2591, 3425 },
                        { 2580, 3425 },
                        { 2579, 3424 },
                        { 2579, 3416 },
                        { 2580, 3415 },
                        { 2583, 3415 },
                        { 2585, 3413 },
                        { 2585, 3410 },
                        { 2586, 3409 },
                        { 2587, 3409 },
                        { 2588, 3408 },
                        { 2588, 3406 },
                        { 2589, 3405 },
                        { 2589, 3401 },
                        { 2592, 3398 },
                        { 2592, 3396 },
                        { 2593, 3395 },
                        { 2594, 3395 },
                        { 2595, 3394 },
                        { 2619, 3394 },
                        { 2624, 3399 },
                        { 2624, 3403 },
                        { 2624, 3449 },
                        { 2595, 3449 }
                })
        };
    }

    @Override
    public String name() {
        return "Fishing Guild";
    }
}
