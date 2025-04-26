package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Kris | 21/04/2019 15:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessEdgevilleDungeon extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3129, 9920 },
                        { 3129, 9920 },
                        { 3135, 9920 },
                        { 3135, 9920 },
                        { 3136, 9920 },
                        { 3136, 10048 },
                        { 3072, 10048 },
                        { 3072, 9920 }
                })
        };
    }

    @Override
    public String name() {
        return "Edgeville Dungeon (Wilderness)";
    }
}
