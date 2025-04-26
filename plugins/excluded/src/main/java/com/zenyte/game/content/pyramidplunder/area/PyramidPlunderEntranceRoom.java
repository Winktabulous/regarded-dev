package com.zenyte.game.content.pyramidplunder.area;

import com.zenyte.game.content.pyramidplunder.PyramidPlunderConstants;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Chris
 * @since May 20 2020
 */
public class PyramidPlunderEntranceRoom extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {1920, 4419},
                        {1985, 4419},
                        {1980, 4482},
                        {1918, 4480}
                }, 3)
        };
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (logout) {
            player.forceLocation(PyramidPlunderConstants.OUTSIDE_PYRAMID);
        }
    }

    @Override
    public String name() {
        return "Pyramid Plunder Entrance";
    }
}
