package com.near_reality.anticheat.tasks;

import com.near_reality.anticheat.NRAntiCheatEngine;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.world.entity.player.Player;

public class SendCS2AntiCheatExecuteTask implements WorldTask {
    private final Player player;

    public SendCS2AntiCheatExecuteTask(Player p) {
        this.player = p;
    }

    @Override
    public void run() {
        if(!DeveloperCommands.INSTANCE.getAnticheatEnabled())
            return;
        player.getPacketDispatcher().sendClientScript(NRAntiCheatEngine.CS2_CALL_INSPECTION);
    }
}