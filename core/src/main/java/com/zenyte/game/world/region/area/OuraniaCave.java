package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OuraniaCave extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3008, 5632 },
                        { 3008, 5568 },
                        { 3072, 5568 },
                        { 3072, 5632 }
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
        return "Ourania Cave";
    }
}
