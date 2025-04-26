package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 01:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KingdomOfMisthalin extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3072, 3520 },
                        { 3072, 3161 },
                        { 3098, 3154 },
                        { 3101, 3147 },
                        { 3105, 3145 },
                        { 3113, 3145 },
                        { 3136, 3145 },
                        { 3140, 3139 },
                        { 3144, 3137 },
                        { 3161, 3133 },
                        { 3200, 3133 },
                        { 3208, 3136 },
                        { 3456, 3136 },
                        { 3456, 3167 },
                        { 3425, 3201 },
                        { 3422, 3215 },
                        { 3422, 3252 },
                        { 3419, 3271 },
                        { 3412, 3286 },
                        { 3414, 3296 },
                        { 3416, 3305 },
                        { 3414, 3316 },
                        { 3401, 3321 },
                        { 3392, 3334 },
                        { 3399, 3393 },
                        { 3402, 3398 },
                        { 3402, 3402 },
                        { 3400, 3407 },
                        { 3406, 3415 },
                        { 3405, 3418 },
                        { 3401, 3423 },
                        { 3401, 3431 },
                        { 3404, 3436 },
                        { 3401, 3446 },
                        { 3405, 3463 },
                        { 3408, 3463 },
                        { 3413, 3468 },
                        { 3416, 3468 },
                        { 3417, 3469 },
                        { 3417, 3470 },
                        { 3418, 3471 },
                        { 3420, 3471 },
                        { 3421, 3472 },
                        { 3421, 3474 },
                        { 3424, 3477 },
                        { 3424, 3479 },
                        { 3422, 3481 },
                        { 3420, 3481 },
                        { 3419, 3482 },
                        { 3419, 3494 },
                        { 3418, 3494 },
                        { 3418, 3495 },
                        { 3416, 3495 },
                        { 3416, 3498 },
                        { 3415, 3498 },
                        { 3413, 3500 },
                        { 3412, 3500 },
                        { 3410, 3502 },
                        { 3410, 3504 },
                        { 3408, 3506 },
                        { 3408, 3508 },
                        { 3407, 3509 },
                        { 3404, 3509 },
                        { 3392, 3509 },
                        { 3392, 3520 }
                })
        };
    }

    @Override
    public void enter(final Player player) {

    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Kingdom of Misthalin";
    }
}
