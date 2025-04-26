package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Morytania extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3392, 3648 },
                        { 3392, 3509 },
                        { 3407, 3509 },
                        { 3408, 3508 },
                        { 3408, 3506 },
                        { 3410, 3504 },
                        { 3410, 3502 },
                        { 3412, 3500 },
                        { 3413, 3500 },
                        { 3415, 3498 },
                        { 3416, 3498 },
                        { 3416, 3495 },
                        { 3418, 3495 },
                        { 3418, 3494 },
                        { 3419, 3494 },
                        { 3419, 3482 },
                        { 3420, 3481 },
                        { 3422, 3481 },
                        { 3424, 3479 },
                        { 3424, 3477 },
                        { 3421, 3474 },
                        { 3421, 3472 },
                        { 3420, 3471 },
                        { 3418, 3471 },
                        { 3417, 3470 },
                        { 3417, 3469 },
                        { 3416, 3468 },
                        { 3413, 3468 },
                        { 3408, 3463 },
                        { 3405, 3463 },
                        { 3405, 3451 },
                        { 3403, 3444 },
                        { 3406, 3439 },
                        { 3406, 3434 },
                        { 3403, 3428 },
                        { 3403, 3425 },
                        { 3407, 3418 },
                        { 3407, 3412 },
                        { 3402, 3407 },
                        { 3405, 3398 },
                        { 3401, 3391 },
                        { 3401, 3388 },
                        { 3406, 3381 },
                        { 3400, 3369 },
                        { 3400, 3328 },
                        { 3401, 3325 },
                        { 3402, 3323 },
                        { 3404, 3322 },
                        { 3419, 3319 },
                        { 3418, 3298 },
                        { 3415, 3292 },
                        { 3415, 3282 },
                        { 3425, 3265 },
                        { 3423, 3262 },
                        { 3423, 3255 },
                        { 3425, 3247 },
                        { 3423, 3245 },
                        { 3423, 3244 },
                        { 3425, 3241 },
                        { 3426, 3236 },
                        { 3423, 3230 },
                        { 3423, 3228 },
                        { 3426, 3219 },
                        { 3424, 3216 },
                        { 3425, 3209 },
                        { 3432, 3204 },
                        { 3456, 3200 },
                        { 3520, 3158 },
                        { 3584, 3136 },
                        { 3840, 3136 },
                        { 3840, 3584 },
                        { 3456, 3584 },
                        { 3456, 3648 }
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
        return "Morytania";
    }
}
