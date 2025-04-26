package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 31/01/2019 03:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WaterbirthDungeon extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2433, 10178 },
                        { 2433, 10109 },
                        { 2564, 10109 },
                        { 2563, 10181 }
                }),
                new RSPolygon(new int[][]{
                        { 1870, 4418 },
                        { 1870, 4342 },
                        { 1976, 4342 },
                        { 1976, 4418 }
                }),
                new RSPolygon(new int[][]{
                        { 2880, 4480 },
                        { 2880, 4352 },
                        { 2944, 4352 },
                        { 2944, 4480 }
                }),
                new RSPolygon(new int[][]{
                        { 1792, 4416 },
                        { 1792, 4352 },
                        { 1984, 4352 },
                        { 1984, 4416 }
                })
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
        return "Waterbirth Dungeon";
    }
}
