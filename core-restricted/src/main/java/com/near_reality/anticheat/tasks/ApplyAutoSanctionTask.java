package com.near_reality.anticheat.tasks;

import com.near_reality.anticheat.NRAntiCheatEngine;
import com.near_reality.api.model.SanctionLevel;
import com.near_reality.api.model.SanctionType;
import com.near_reality.api.service.sanction.SanctionPlayerHandler;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.tools.CoroutineJvmContinuation;
import com.zenyte.utils.TimeUnit;
import dev.kord.core.entity.channel.TextChannel;
import kotlin.Unit;

import java.util.concurrent.CompletableFuture;

/**
 * This event is queued when a detection is made. If auto-sanctions are toggled,
 * the sanction is applied after processAtTick is met, otherwise, sends message
 * the staff discord via Xamphur bot immediately
 */
public class ApplyAutoSanctionTask extends TickTask {
    private final Player player;
    private final String command;
    private final int processAtTick;

    public ApplyAutoSanctionTask(Player player, String cmd) {
        this.player = player;
        this.command = cmd;
        processAtTick = Utils.random(Long.valueOf(TimeUnit.MINUTES.toTicks(NRAntiCheatEngine.RNG_START_BOUND_MINUTES)).intValue(), Long.valueOf(TimeUnit.MINUTES.toTicks(NRAntiCheatEngine.RNG_END_BOUND_MINUTES)).intValue());
    }

    @Override
    public void run() {
        if(!DeveloperCommands.INSTANCE.getAnticheatEnabled())
            return;
        NRAntiCheatEngine.getEngine().sanctionQueued(player);
        if(!DeveloperCommands.INSTANCE.getAutomatedSanctions()) {
            CompletableFuture<Unit> suspendResult = new CompletableFuture<>();
            NRAntiCheatEngine.sanctionBot.sendAutoDetection(player, command, false, new CoroutineJvmContinuation<>(suspendResult));
            stop();
            return;
        }

        if(ticks == processAtTick) {
            SanctionPlayerHandler.submitSanction(SanctionLevel.ACCOUNT, SanctionType.BAN, player, null, "Automated Detection");
            CompletableFuture<Unit> suspendResult = new CompletableFuture<>();
            NRAntiCheatEngine.sanctionBot.sendAutoDetection(player, command, true, new CoroutineJvmContinuation<>(suspendResult));
            stop();
            return;
        }
        ticks++;
    }
}
