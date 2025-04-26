package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Tirannwn extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2112, 3456 },
                        { 2112, 3008 },
                        { 2304, 3008 },
                        { 2304, 3072 },
                        { 2325, 3072 },
                        { 2334, 3080 },
                        { 2368, 3080 },
                        { 2368, 3328 },
                        { 2304, 3328 },
                        { 2304, 3456 }
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
        return "Tirannwn";
    }
}
