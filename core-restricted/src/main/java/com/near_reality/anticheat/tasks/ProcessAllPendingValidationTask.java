package com.near_reality.anticheat.tasks;

import com.near_reality.anticheat.NRAntiCheatEngine;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;

public class ProcessAllPendingValidationTask implements WorldTask {

    @Override
    public void run() {
        if(!DeveloperCommands.INSTANCE.getAnticheatEnabled())
            return;
        NRAntiCheatEngine.getEngine().getPendingValidationPlayers().forEach(it -> {
            WorldTasksManager.schedule(new SendCS2AntiCheatSetTask(it), 0, -1);
            NRAntiCheatEngine.getEngine().addPlayerToAwaitingResponse(it);
            WorldTasksManager.schedule(new SendCS2AntiCheatExecuteTask(it), 6, -1);
        });
        NRAntiCheatEngine.getEngine().clearPendingValidation();
    }
}
