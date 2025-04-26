package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 17/08/2019 15:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChambersOfXericArea extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3200, 5760 },
                        { 3392, 5760 },
                        { 3392, 5120 },
                        { 3264, 5120 },
                        { 3264, 5696 },
                        { 3200, 5696 }
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
        return "Chambers of Xeric";
    }
}
