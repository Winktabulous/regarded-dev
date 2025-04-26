package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 31/01/2019 01:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrongholdOfSecurity extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1854, 5248 },
                        { 1854, 5182 },
                        { 1920, 5182 },
                        { 1920, 5248 }
                }, 0),
                new RSPolygon(new int[][]{
                        { 1980, 5250 },
                        { 1980, 5180 },
                        { 2050, 5180 },
                        { 2050, 5250 }
                }, 0),
                new RSPolygon(new int[][]{
                        { 2108, 5312 },
                        { 2108, 5245 },
                        { 2180, 5245 },
                        { 2180, 5312 }
                }, 0),
                new RSPolygon(new int[][]{
                        { 2304, 5248 },
                        { 2304, 5184 },
                        { 2368, 5184 },
                        { 2368, 5248 }
                }, 0)
        };
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Stronghold of Security";
    }
}
