package com.near_reality.anticheat.tasks;

import com.near_reality.anticheat.NRAntiCheatEngine;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;

public class QueueAllSanctionsTask implements WorldTask {
    @Override
    public void run() {
        if(!DeveloperCommands.INSTANCE.getAnticheatEnabled())
            return;
        NRAntiCheatEngine.getEngine().getSanctionQueue().forEach((player, cmd) -> {
            WorldTasksManager.schedule(new ApplyAutoSanctionTask(player, cmd), 0, -1);
        });
    }
}
