package com.zenyte.game.content.wildernessVault;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.plugins.events.ServerLaunchEvent;

public class WildernessVault {

    @Subscribe
    public static void boot(ServerLaunchEvent event) {
        WorldTasksManager.schedule(WildernessVaultHandler.getInstance(), 1, 0);
    }
}
