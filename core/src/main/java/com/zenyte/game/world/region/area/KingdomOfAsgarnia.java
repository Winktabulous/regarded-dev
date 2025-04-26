package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 01:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KingdomOfAsgarnia extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2944, 3520 },
                        { 2944, 3392 },
                        { 2937, 3388 },
                        { 2880, 3372 },
                        { 2880, 3194 },
                        { 2944, 3194 },
                        { 2969, 3156 },
                        { 2969, 3072 },
                        { 3034, 3072 },
                        { 3034, 3136 },
                        { 3072, 3154 },
                        { 3072, 3520 }
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
        return "Kingdom of Asgarnia";
    }
}
