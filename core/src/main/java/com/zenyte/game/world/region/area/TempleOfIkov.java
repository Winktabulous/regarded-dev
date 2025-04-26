package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 14:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TempleOfIkov extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2624, 9920 },
                        { 2624, 9792 },
                        { 2681, 9792 },
                        { 2681, 9783 },
                        { 2688, 9783 },
                        { 2688, 9792 },
                        { 2704, 9792 },
                        { 2704, 9831 },
                        { 2736, 9831 },
                        { 2736, 9818 },
                        { 2752, 9818 },
                        { 2752, 9856 },
                        { 2688, 9856 },
                        { 2688, 9920 }
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
        return "Temple of Ikov";
    }
}
