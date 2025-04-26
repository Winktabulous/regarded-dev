package com.zenyte.game.content.larranskey;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.World;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.ServerLaunchEvent;

public class LarransKeyModule {

    @Subscribe
    public static void on(ServerLaunchEvent event) {
        final WorldObject smallLarensChest = new WorldObject(LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID, 10, 1, 3281, 3659, 0);
        World.spawnObject(smallLarensChest);

        final WorldObject largeChest = new WorldObject(LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID, 10, 2, 3018, 3955, 1);
        World.spawnObject(largeChest);


    }


}
