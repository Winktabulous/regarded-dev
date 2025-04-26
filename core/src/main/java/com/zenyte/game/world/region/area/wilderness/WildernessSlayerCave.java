package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.region.RSPolygon;

public class WildernessSlayerCave extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3316, 10041 },
                        { 3316, 10176 },
                        { 3470, 10041 },
                        { 3470, 10176 }
                })
        };
    }

    @Override
    public String name() {
        return "Wilderness Slayer Cave";
    }
}
