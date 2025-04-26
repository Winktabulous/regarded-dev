package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KingdomOfKandarin extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2240, 3712 },
                        { 2240, 3456 },
                        { 2304, 3456 },
                        { 2304, 3328 },
                        { 2368, 3328 },
                        { 2368, 3136 },
                        { 2416, 3136 },
                        { 2432, 3120 },
                        { 2432, 3072 },
                        { 2688, 3072 },
                        { 2688, 3264 },
                        { 2752, 3264 },
                        { 2752, 3384 },
                        { 2778, 3392 },
                        { 2944, 3392 },
                        { 2944, 3444 },
                        { 2940, 3448 },
                        { 2938, 3448 },
                        { 2937, 3449 },
                        { 2936, 3449 },
                        { 2936, 3454 },
                        { 2937, 3455 },
                        { 2939, 3455 },
                        { 2940, 3456 },
                        { 2940, 3472 },
                        { 2937, 3475 },
                        { 2937, 3493 },
                        { 2935, 3495 },
                        { 2935, 3503 },
                        { 2938, 3506 },
                        { 2938, 3507 },
                        { 2936, 3509 },
                        { 2929, 3509 },
                        { 2927, 3511 },
                        { 2927, 3517 },
                        { 2925, 3518 },
                        { 2925, 3520 },
                        { 2764, 3520 },
                        { 2763, 3584 },
                        { 2560, 3584 },
                        { 2560, 3712 }
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
        return "Kingdom of Kandarin";
    }
}
