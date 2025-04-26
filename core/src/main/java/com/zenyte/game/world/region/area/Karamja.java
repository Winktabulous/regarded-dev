package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 00:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Karamja extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2688, 3258 },
                        { 2688, 2880 },
                        { 3008, 2880 },
                        { 3008, 3072 },
                        { 2944, 3072 },
                        { 2944, 3136 },
                        { 2961, 3136 },
                        { 2961, 3164 },
                        { 2944, 3164 },
                        { 2944, 3194 },
                        { 2880, 3194 },
                        { 2880, 3212 },
                        { 2816, 3212 },
                        { 2816, 3258 }
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
        return "Karamja";
    }
}
