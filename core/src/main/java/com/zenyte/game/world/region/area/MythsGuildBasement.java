package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 20:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsGuildBasement extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1920, 9088 },
                        { 1920, 9024 },
                        { 1856, 9024 },
                        { 1856, 8960 },
                        { 2112, 8960 },
                        { 2112, 9024 },
                        { 1984, 9024 },
                        { 1984, 9088 }
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
        return "Myth's Guild Basement";
    }
}
