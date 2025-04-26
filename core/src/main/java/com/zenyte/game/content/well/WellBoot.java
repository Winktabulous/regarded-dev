package com.zenyte.game.content.well;

import com.google.common.eventbus.Subscribe;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.plugins.events.ServerLaunchEvent;

public class WellBoot {

    @Subscribe
    public static void boot(ServerLaunchEvent event) {
        WorldTasksManager.schedule(WellHandler.get(), 1, 0);

        CoresManager.getServiceProvider().scheduleRepeatingTask(() -> {
            try {
                WellHandler.get().saveState();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }, 30, 30);
    }
}
