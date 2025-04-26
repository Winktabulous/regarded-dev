package com.zenyte.game.world;

import com.near_reality.game.content.bountyhunter.BountyHunterController;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.near_reality.game.world.PlayerEvent;
import com.near_reality.game.world.WorldEvent;
import com.near_reality.game.world.WorldHooks;
import com.near_reality.threads.MainThread;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameClock;
import com.zenyte.game.GameConstants;
import com.zenyte.game.model.BonusXpManager;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogoutType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.login.LoginManager;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PrebuiltDynamicAreaManager;
import com.zenyte.net.Session;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public final class WorldThread extends MainThread {

    private static final Logger log = LoggerFactory.getLogger(WorldThread.class);
    private static final Logger tickLogger = LoggerFactory.getLogger("Tick Logger");
    public static final int CHUNK_PURGE_TICK_INTERVAL = 500;

    private final WorldHooks hooks = new WorldHooks();

    public static long WORLD_CYCLE;
    private static boolean purgeMoreChunksNextCycle;
    public static volatile long nano = System.nanoTime();
    private static int pidSwapDelay = randomPIDSwapDelay();

    public WorldThread(@NotNull String name) {
        super(name);
    }

    private static int randomPIDSwapDelay() {
        return Utils.random(100, 150);
    }

    public static long getCurrentCycle() {
        return WORLD_CYCLE;
    }

    private static boolean skipPlayer(Player player) {
        return player == null || player.isNulled() || !player.isInitialized() || player.isFinished();
    }

    @Override
    public void cycle() {
        try {
            worldCycle();
        } catch (final Throwable t) {
            log.error("Failed to complete world cycle", t);
        }
    }

    private void worldCycle() {
        nano = System.nanoTime();

        WORLD_CYCLE++;

        try {
            if (hooks.hasListenersFor(WorldEvent.Tick.class))
                hooks.post(new WorldEvent.Tick(WORLD_CYCLE));
        } catch (final Throwable t) {
            log.error("Failed to post world ticK", t);
        }

        try {
            Container.resetContainer();
        } catch (final Throwable e) {
            log.error("Failed to reset containers", e);
        }

        final long shopNano = System.nanoTime();
        try {
            Shop.process();
        } catch (final Throwable e) {
            log.error("Failed to process shops", e);
        }

        final long areaManagerNano = System.nanoTime();
        try {
            GlobalAreaManager.process();
        } catch (final Throwable e) {
            log.error("Failed to process global areas", e);
        }

        try {
            PrebuiltDynamicAreaManager.process();
        } catch (final Throwable e) {
            log.error("Failed to process advanced dynamic areas", e);
        }

        final long worldTaskProcessNano = System.nanoTime();
        try {
            WorldTasksManager.processTasks();
        } catch (final Throwable e) {
            log.error("Failed to process world tasks", e);
        }

        try {
            BountyHunterController.process();
        } catch (final Throwable e) {
            log.error("Failed to process bounty hunter", e);
        }

        final long gameClockNano = System.nanoTime();
        try {
            GameClock.process();
            BonusXpManager.checkIfFlip();
        } catch (final Exception e) {
            log.error("Failed to process gameclock and bonus xp manager", e);
        }

        final long readPacketsNano = System.nanoTime();
        processPlayerSessions();

        final long npcProcessNano = System.nanoTime();
        processNPCs(npcProcessNano);

        final long npcRemovalNano = System.nanoTime();
        processNPCRemoval();

        try {
            NPC.clearPendingAggressions();
        } catch (final Throwable e) {
            log.error("Failed to clear pending aggressions", e);
        }

        final long playerProcessNano = System.nanoTime();
        processPlayers();

        updatePlayerFollowTiles();
        processPlayerFollowing();
        final long playerPostProcessNano = System.nanoTime();
        postProcessPlayers();
        final long areaPostProcessNano = System.nanoTime();
        try {
            GlobalAreaManager.postProcess();
        } catch (final Throwable e) {
            log.error("Failed to post process global areas", e);
        }
        final long playerEntityUpdateNano = System.nanoTime();
        processPlayerEntityUpdate();

        final long playerLogoutNano = System.nanoTime();
        processPlayerLogouts();

        final long maskResetNano = System.nanoTime();
        resetNpcMasks();

        resetPlayerMasks();

        //World.getQueueList().cycle();

        shufflePids();

        final long purgeNano = System.nanoTime();

        purgeWorldChunks();

        final long playerSaveNano = System.nanoTime();
        savePlayers();

        final long finishNano = System.nanoTime();

        final long totalT = finishNano - nano;
        final long totalMillis = TimeUnit.NANOSECONDS.toMillis(totalT);
        final boolean slowTick = totalMillis > GameConstants.WORLD_CYCLE_TIME / 2;
        final int playerCount = World.getPlayers().size();
        final int npcCount = World.getNPCs().size();
        final int taskCount = WorldTasksManager.getCount();

        if (slowTick) {
            log.error("SLOW CYCLE took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
        }

        if (slowTick || GameConstants.CYCLE_DEBUG) {
            final TickLog tickLog = new TickLog(
                    new Date(),
                    WorldThread.WORLD_CYCLE,
                    NANOSECONDS.toMillis(shopNano - nano),
                    NANOSECONDS.toMillis(areaManagerNano - shopNano),
                    NANOSECONDS.toMillis(worldTaskProcessNano - areaManagerNano),
                    NANOSECONDS.toMillis(gameClockNano - worldTaskProcessNano),
                    NANOSECONDS.toMillis(readPacketsNano - gameClockNano),
                    NANOSECONDS.toMillis(npcProcessNano - readPacketsNano),
                    NANOSECONDS.toMillis(npcRemovalNano - npcProcessNano),
                    NANOSECONDS.toMillis(playerProcessNano - npcRemovalNano),
                    NANOSECONDS.toMillis(playerPostProcessNano - playerProcessNano),
                    NANOSECONDS.toMillis(areaPostProcessNano - playerPostProcessNano),
                    NANOSECONDS.toMillis(playerEntityUpdateNano - areaPostProcessNano),
                    NANOSECONDS.toMillis(playerLogoutNano - playerEntityUpdateNano),
                    NANOSECONDS.toMillis(maskResetNano - playerLogoutNano),
                    NANOSECONDS.toMillis(purgeNano - maskResetNano),
                    NANOSECONDS.toMillis(WORLD_CYCLE % 500 == 0 ? (playerSaveNano - purgeNano) : 0),
                    NANOSECONDS.toMillis(finishNano - playerSaveNano),
                    NANOSECONDS.toMillis(totalT),
                    playerCount,
                    npcCount,
                    taskCount);
            if (GameConstants.CYCLE_DEBUG) {
                tickLogger.warn("Cycle took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
            } else
                tickLogger.info("Cycle took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
            ForkJoinPool.commonPool().execute(() -> {
                final String tickGSON = DefaultGson.getGson().toJson(tickLog);
                if (slowTick) tickLogger.error(tickGSON);
                else if (GameConstants.CYCLE_DEBUG) tickLogger.warn(tickGSON);
                else tickLogger.info(tickGSON);
            });
        }

        if (CoresManager.isShutdown()) {
            try {
                World.shutdown();
            } catch (Throwable e) {
                log.error("Failed to shutdown work", e);
            }
        }
    }

    private void processPlayerSessions() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) {
                        if (player.isDeveloper())
                            log.warn("Skipping player {} isNulled={}, isInitialized={}, isFinished={}",
                                    player, player.isNulled(), player.isInitialized(), player.isFinished());
                        continue;
                    }
                    final Session session = player.getSession();
                    if (session != null) {
                        session.process();
                    } else
                        log.error("Player {} has no session", player);
                } catch (final Throwable e) {
                    log.error("Failed to process player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process player sessions", e);
        }
    }

    private void processNPCs(final long npcProcessNano) {
        long singleNpcProcessNano = npcProcessNano;
        try {
            for (final NPC npc : World.getNPCs()) {
                try {
                    if (npc == null) continue;

                    npc.processEntity();

                    if (DeveloperCommands.INSTANCE.getNpcProcessTimeLogging()) {
                        final long currentTime = System.nanoTime();
                        final long npcDuration = singleNpcProcessNano - currentTime;
                        singleNpcProcessNano = currentTime;
                        if (npcDuration > 1_000_000) {
                            log.warn(
                                    "Slow NPC.processEntity() invocation ({}ms) for NPC (id = {}, index = {}, " +
                                            "location = {})",
                                    NANOSECONDS.toMillis(npcDuration), npc.getId(), npc.getIndex(),
                                    npc.getLocation());
                        }
                    }
                } catch (final Throwable e) {
                    log.error("Failed to process NPC (id = {}, index = {}, location = {})", npc.getId(),
                            npc.getIndex(), npc.getLocation(), e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process NPCs", e);
        }
    }

    private void processNPCRemoval() {
        try {
            for (final NPC removed : World.pendingRemovedNPCs) {
                try {
                    if (removed == null) continue;

                    World.getNPCs().remove(removed);
                } catch (final Throwable e) {
                    log.error("Failed to remove npc {}", removed, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to remove {} NPCs pending for removal", World.pendingRemovedNPCs.size(), e);
        }

        try {
            World.pendingRemovedNPCs.clear();
        } catch (final Throwable e) {
            log.error("Failed to clear {} NPCs pending for removal", World.pendingRemovedNPCs.size(), e);
        }
    }

    private void processPlayers() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.Process.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.Process(player));
                    }
                    player.processEntity();
                } catch (final Throwable e) {
                    log.error("Failed to process player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process players", e);
        }
    }

    private void updatePlayerFollowTiles() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    player.updateFollowTile();
                } catch (final Throwable e) {
                    log.error("Failed ot update follow tile for player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to update follow tiles", e);
        }
    }

    private void processPlayerFollowing() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    player.processFollowing();
                } catch (final Throwable e) {
                    log.error("Failed to process following for player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process following for players", e);
        }
    }

    private void postProcessPlayers() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.PostProcess.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.PostProcess(player));
                    }

                    player.postProcess();
                } catch (final Throwable e) {
                    log.error("Failed to post process player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to post process players", e);
        }
    }

    private void processPlayerEntityUpdate() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.Update.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.Update(player));
                    }
                    player.processEntityUpdate();
                } catch (final Throwable e) {
                    log.error("Failed to perform entity update for player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to perform entity update for players", e);
        }
    }

    private void processPlayerLogouts() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;
                    if (player.isSessionExpired()) {
                        if (player.isDeveloper())
                            log.info("Player {} has an expired session, unregistering...", player);
                        World.unregisterPlayer(player);
                    } else {
                        final LogoutType logoutType = player.getLogoutType();
                        if (LogoutType.CONNECTION_LOST.equals(logoutType)) {
                            if (player.isDeveloper())
                                log.trace("Player {} has lost connection, waiting for session to expire...", player);
                        } else if (LogoutType.FORCE.equals(logoutType) || LogoutType.UPDATING.equals(logoutType)) {
                            if (player.isDeveloper())
                                log.info("Player {} is being forcefully logged out", player);
                            player.getPacketDispatcher().sendLogout();
                            player.getSession().flush();
                            World.unregisterPlayer(player);
                        } else {
                            if (LogoutType.REQUESTED.equals(logoutType)) {
                                if (player.canLogout(false)) {
                                    if (player.isDeveloper())
                                        log.info("Player {} is being logged out", player);
                                    player.getPacketDispatcher().sendLogout();
                                    World.unregisterPlayer(player);
                                    continue;
                                } else {
                                    if (player.isDeveloper())
                                        log.info("Player {}'s logout has been cancelled as they currently cannot.", player);
                                    player.setLogoutType(LogoutType.NONE);
                                }
                            }
                            player.getSession().flush();
                        }
                    }
                } catch (final Throwable e) {
                    log.error("Failed to logout player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to logout players", e);
        }
    }

    private void resetNpcMasks() {
        try {
            for (final NPC npc : World.getNPCs()) {
                try {
                    if (npc == null || npc.isFinished()) {
                        continue;
                    }
                    npc.resetMasks();
                } catch (final Throwable e) {
                    log.error("Failed to reset masks for NPC {}", npc, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to reset NPC masks", e);
        }
    }

    private void resetPlayerMasks() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (player == null || player.isFinished()) continue;
                    player.resetMasks();
                } catch (final Throwable e) {
                    log.error("Failed to reset masks for player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to reset player masks.", e);
        }
    }

    private void shufflePids() {
        try {
            if (--pidSwapDelay == 0) {
                try {
                    World.shufflePids();
                } catch (final Throwable e) {
                    log.error("Failed to shuffle PIDs, inner", e);
                }
                pidSwapDelay = randomPIDSwapDelay();
            }
        } catch (final Throwable e) {
            log.error("Failed to shuffle PIDs, outer", e);
        }
    }

    private void purgeWorldChunks() {
        try {
            if (GameConstants.PURGING_CHUNKS) {
                if (WORLD_CYCLE % CHUNK_PURGE_TICK_INTERVAL == 0 || purgeMoreChunksNextCycle) {
                    purgeMoreChunksNextCycle = World.purgeChunks();
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to purge world chunks", e);
        }
    }

    private void savePlayers() {
        try {
            final LoginManager loginManager = CoresManager.getLoginManager();
            final Set<Player> awaitingSave = loginManager.getAwaitingSave();
            if (!awaitingSave.isEmpty()) {
                for (final Player player : awaitingSave) {
                    loginManager.save(player);
                }
                awaitingSave.clear();
            }
        } catch (final Throwable e) {
            log.error("Failed to save players", e);
        }
    }

    public record TickLog(Date date, long tick, long containerT, long shopT, long areaT, long worldTaskT,
                          long gameClockT, long readPacketsT, long npcProcessT, long npcRemovalT, long playerProcessT,
                          long playerPostProcessNano, long areaPostProcessNano, long playerEntityUpdateNano,
                          long playerLogoutT, long maskResetT, long purgeT, long playerSaveT, long totalT, int players,
                          int npcs, int tasks) {
    }

    public WorldHooks getHooks() {
        return hooks;
    }

}
