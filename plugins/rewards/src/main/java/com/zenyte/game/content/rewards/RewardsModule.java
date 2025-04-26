package com.zenyte.game.content.rewards;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.WorldThread;
import com.zenyte.plugins.events.ServerLaunchEvent;

public final class RewardsModule {

    @Subscribe
    public static void on(ServerLaunchEvent event) {
        start(event.getWorldThread());
    }

    private static void start(WorldThread thread) {
        Rewards.load();
    }


}
