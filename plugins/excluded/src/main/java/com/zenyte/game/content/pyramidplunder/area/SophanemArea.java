package com.zenyte.game.content.pyramidplunder.area;

import com.zenyte.game.content.pyramidplunder.object.AnonymousDoor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.utils.TimeUnit;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class SophanemArea extends PolygonRegionArea implements CycleProcessPlugin {
    private static final long DOOR_SHUFFLE_DELAY = TimeUnit.MINUTES.toTicks(3);

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {3276, 2752},
                        {3324, 2752},
                        {3324, 2810},
                        {3268, 2810}
                })
        };
    }

    @Override
    public void process() {
        if (getAreaTimer() % DOOR_SHUFFLE_DELAY == 0) {
            AnonymousDoor.shuffleAnonymousDoor();
        }
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Sophanem";
    }
}
