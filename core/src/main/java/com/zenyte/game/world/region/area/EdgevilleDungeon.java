package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 14:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleDungeon extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3152, 9920 },
                        { 3152, 9856 },
                        { 3136, 9856 },
                        { 3136, 9821 },
                        { 3087, 9821 },
                        { 3087, 9920 },
                        { 3129, 9920 },
                        { 3129, 9918 },
                        { 3135, 9918 },
                        { 3135, 9920 }
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
        return "Edgeville Dungeon";
    }
}
