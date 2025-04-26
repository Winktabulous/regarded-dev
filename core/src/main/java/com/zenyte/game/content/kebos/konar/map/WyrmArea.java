package com.zenyte.game.content.kebos.konar.map;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Tommeh | 24/10/2019 | 23:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WyrmArea extends KaruulmSlayerDungeon implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{
                { 1246, 10162 },
                { 1255, 10143 },
                { 1281, 10156 },
                { 1281, 10175 },
                { 1296, 10189 },
                { 1296, 10211 },
                { 1277, 10206 },
                { 1257, 10205 }
        }, 0)};
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon (Wyrms)";
    }

}
