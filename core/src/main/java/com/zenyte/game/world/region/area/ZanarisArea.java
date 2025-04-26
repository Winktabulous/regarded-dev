package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 20:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZanarisArea extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2304, 4480 },
                        { 2304, 4416 },
                        { 2368, 4416 },
                        { 2368, 4352 },
                        { 2432, 4352 },
                        { 2432, 4416 },
                        { 2496, 4416 },
                        { 2496, 4480 }
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
        return "Zanaris";
    }
}
