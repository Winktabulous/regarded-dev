package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 13:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Weiss extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2796, 3968 },
                        { 2797, 3891 },
                        { 2816, 3891 },
                        { 2816, 3840 },
                        { 2944, 3840 },
                        { 2944, 3968 },
                        { 2865, 3968 },
                        { 2861, 3972 },
                        { 2848, 3972 },
                        { 2843, 3968 }
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
        return "Weiss";
    }
}
