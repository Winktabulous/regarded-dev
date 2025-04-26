package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 20:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArdougneSewers extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2513, 9728 },
                        { 2513, 9635 },
                        { 2560, 9635 },
                        { 2560, 9664 },
                        { 2624, 9664 },
                        { 2624, 9728 }
                }), new RSPolygon(new int[][]{
                { 2624, 9728 },
                { 2624, 9600 },
                { 2688, 9600 },
                { 2688, 9728 }
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
        return "Ardougne Sewers";
    }
}
