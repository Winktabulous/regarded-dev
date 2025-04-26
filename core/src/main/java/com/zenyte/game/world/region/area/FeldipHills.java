package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FeldipHills extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2432, 3072 },
                        { 2432, 2816 },
                        { 2688, 2816 },
                        { 2688, 3072 },
                        { 2384, 3072 },
                        { 2368, 3088 },
                        { 2368, 3080 },
                        { 2335, 3080 },
                        { 2325, 3072 },
                        { 2304, 3072 },
                        { 2304, 3008 },
                        { 2432, 3008 }
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
        return "Feldip Hills";
    }
}
