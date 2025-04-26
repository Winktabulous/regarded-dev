package com.near_reality.anticheat;

import com.google.common.eventbus.Subscribe;
import com.near_reality.anticheat.tasks.ProcessAllPendingValidationTask;
import com.near_reality.anticheat.tasks.QueueAllSanctionsTask;
import com.near_reality.game.world.info.WorldProfile;
import com.near_reality.tools.discord.DiscordBot;
import com.near_reality.tools.discord.DiscordServer;
import com.zenyte.game.GameConstants;
import com.zenyte.game.packet.in.event.PlayerReportEvent;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.GameConstants.WORLD_PROFILE;

/**
 * This is the main engine for handling all anti-cheat processing, detection and response
 * @author John J. Woloszyk
 */
public class NRAntiCheatEngine {

    public static Logger log = LoggerFactory.getLogger(NRAntiCheatEngine.class);

    public boolean debug;

    /**
     * The varcstr that holds the inspection command, set by CS2
     */
    public static int VARCSTR_PTR = 690;

    /**
     * CS2 ID for setting VARCSTR
     */
    public static int CS2_SET_INSPECTION = 45998;

    /**
     * CS2 ID for custom mixin that pulls VARCSTR and returns a phony report abuse
     * packet that we are expecting within 1 minute after sending.
     */
    public static int CS2_CALL_INSPECTION = 45999;

    /**
     * How often the detection events are run and processed
     */
    public static final int CYCLE_PROCESS_MINUTES = 5;

    /**
     * The minimum amount of minutes after a detection is made that
     * the sanction is automatically applied
     */
    public static final int RNG_START_BOUND_MINUTES = 1;

    /**
     * The maximum amount of minutes after a detection is made that
     * the sanction is automatically applied
     */
    public static final int RNG_END_BOUND_MINUTES = 10;
    public static DiscordBot sanctionBot;

    public NRAntiCheatEngine() {
        debug = WORLD_PROFILE.isDevelopment();
        validatedPlayers = new ObjectArrayList<>(2048);
        pendingValidationPlayers = new ObjectArrayList<>(2048);
        awaitingValidationResponse = new ObjectArrayList<>(2048);
        sanctionQueue = new Object2ObjectArrayMap<>(2048);
    }

    public ObjectArrayList<Player> validatedPlayers;
    public ObjectArrayList<Player> pendingValidationPlayers;
    public ObjectArrayList<Player> awaitingValidationResponse;
    public Object2ObjectArrayMap<Player, String> sanctionQueue;

    private static NRAntiCheatEngine antiCheatEngine;
    @Subscribe
    public static void onInitialization(ServerLaunchEvent event) {
        antiCheatEngine = new NRAntiCheatEngine();
        queueEvents();
        if (WORLD_PROFILE.isDiscordEnabled()) {
            sanctionBot = new DiscordBot();
            sanctionBot.initStaff("MTIxOTM5Nzc1NTkwMzE0ODAzMg.GO-Q32.xHiNkByNMzV_D2BtGt2Yvhrq2ytjAdiRzHSyWE");
        }
    }

    private static void queueEvents() {
        /* Rolls through all pending validation at fixed rate */
        WorldTasksManager.schedule(new ProcessAllPendingValidationTask(),100, 100);
        /* Rolls through all pending sanctions every minute and schedules one-off subtasks */
        WorldTasksManager.schedule(new QueueAllSanctionsTask(), 100, 100);
    }

    @Subscribe
    public static void onPhonyReport(PlayerReportEvent event) {
        if(antiCheatEngine.debug)
            log.debug("Player report received: {} as {}", event.source.getUsername(), event.getAppliedName());
        Player source = event.source;
        antiCheatEngine.removePlayerFromAwaitingResponse(source);

        String cmd = event.getAppliedName();
        boolean invalidCommand = isBotEngineCommand(cmd);

        if(debugPlayer(source)) {
            invalidCommand = true;
            cmd = "simple.Loader --NearReality";
        }

        if(invalidCommand) {
            antiCheatEngine.queueSanction(source, cmd);
            return;
        }
        antiCheatEngine.playerValidated(source);
    }

    private static boolean debugPlayer(Player source) {
        if(source.getUsername().equalsIgnoreCase("botter69420"))
            return true;

        return false;
    }

    private void playerValidated(Player source) {
        if(antiCheatEngine.debug)
            log.debug("Player validated {}", source.getUsername());
        validatedPlayers.add(source);
    }

    private void queueSanction(Player source, String cmd) {
        if(antiCheatEngine.debug)
            log.debug("Player sanction queued {}", source.getUsername());
        sanctionQueue.put(source, cmd);
    }

    public void sanctionQueued(Player sanctioned) {
        sanctionQueue.remove(sanctioned);
    }

    private static boolean isBotEngineCommand(String cmd) {
        if(cmd.contains("simple"))
            return true;
        if(cmd.contains(".Loader"))
            return true;
        if(cmd.contains("simplebot-api.jar"))
            return true;
        return false;
    }

    @Subscribe
    public static void onPlayerLogout(LogoutEvent event) {
        if(antiCheatEngine.debug)
            log.debug("Player logout {}", event.getPlayer().getUsername());
        if(antiCheatEngine != null)
            antiCheatEngine.removePlayer(event.getPlayer());
    }

    @Subscribe
    public static void onPlayerLogin(LoginEvent event) {
        if(antiCheatEngine.debug)
            log.debug("Player login {}", event.getPlayer().getUsername());
        if(antiCheatEngine != null)
            antiCheatEngine.addPlayerToPendingValidation(event.getPlayer());
    }

    public static NRAntiCheatEngine getEngine() {
        return antiCheatEngine;
    }

    public void addPlayerToPendingValidation(Player player) {
        if(antiCheatEngine.debug)
            log.debug("Player added to pending validation {}", player.getUsername());
        pendingValidationPlayers.add(player);
    }

    public void addPlayerToAwaitingResponse(Player player) {
        if(antiCheatEngine.debug)
            log.debug("Player added to waiting response {}", player.getUsername());
        awaitingValidationResponse.add(player);
    }

    public void removePlayerFromAwaitingResponse(Player player) {
        if(antiCheatEngine.debug)
            log.debug("Player removed from waiting response {}", player.getUsername());
        awaitingValidationResponse.remove(player);
    }

    public void removePlayer(Player player) {
        validatedPlayers.remove(player);
        pendingValidationPlayers.remove(player);
    }

    public ObjectArrayList<Player> getPendingValidationPlayers() {
        return pendingValidationPlayers;
    }

    public void clearPendingValidation() {
        pendingValidationPlayers.clear();
    }

    public Object2ObjectArrayMap<Player, String> getSanctionQueue() {
        return sanctionQueue;
    }
}
