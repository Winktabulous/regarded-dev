package com.zenyte.game.content.xamphur;

import com.google.common.eventbus.Subscribe;
import com.near_reality.tools.discord.community.DiscordBroadcastKt;
import com.near_reality.tools.discord.community.DiscordCommunityBot;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TimeUnit;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class XamphurHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XamphurHandler.class);
    private static final int BOOST_HOURS = 2;
    private static final int AMT_TO_SPAWN = 50;
    public static final long XAMPHUR_SPAWN_DELAY_IN_SECONDS = 30L;
    private WorldTask xamphurSpawnTask;

    private int votes;

    private boolean enabled = true;

    private final Xamphur xamphur = new Xamphur();

    public static XamphurHandler get() {
        return instance;
    }

    private int announcements;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addVotes(Player player, int amt) {
        if(!enabled)
            return;

        votes +=amt;

        if(votes >= AMT_TO_SPAWN) {
            votes = 0;
            announcements = 0;
            if (xamphurSpawnTask != null) {
                LOGGER.warn("Cancelling previous xamphur spawn task in favor of new one.");
                WorldTasksManager.stop(xamphurSpawnTask);
            }
            xamphurSpawnTask = () -> {
                WorldBroadcasts.sendMessage("<img=22> The World Boss has spawned at ::event", BroadcastType.XAMPHUR, true);
                xamphur.start();
            };
            WorldBroadcasts.sendMessage("<img=22> Xamphur will spawn in 60 seconds at ::event", BroadcastType.XAMPHUR, true);
            WorldTasksManager.schedule(xamphurSpawnTask, (int) TimeUnit.SECONDS.toTicks(XAMPHUR_SPAWN_DELAY_IN_SECONDS));
        } else {

            if(votes >= 49 && announcements == 0 || votes >= 89 && announcements == 1) {
                int announcedAmount = switch (announcements) {
                    case 0 -> 50;
                    case 1 -> 10;
                    default -> 0;
                };
                WorldBroadcasts.sendMessage("<img=" + 22 + "><col=" + "e59400" + ">" + "<shad=000000>" + "Event: " + player.getName() + " has claimed " + amt + " votes, " + announcedAmount + " more votes until Xamphur spawns!", BroadcastType.XAMPHUR, false);
                DiscordBroadcastKt.onVotesMilestone(DiscordCommunityBot.INSTANCE, player, amt, amtTillSpawn());
                announcements++;
            }
        }
    }

    public Xamphur getXamphur() {
        return xamphur;
    }

    public int amtTillSpawn() {
        return AMT_TO_SPAWN - votes;
    }


    private static XamphurHandler instance;


    public void activateRandomInactiveWorldBoost() {
        if(!enabled) {
            return;
        }

        List<XamphurBoost> availableBoosts = new ArrayList<>(List.of(XamphurBoost.VALUES));
        availableBoosts.removeIf(World::hasBoost);

        XamphurBoost random = Utils.random(availableBoosts);
        if (random == null) {
            return;
        }

        activateBoost(random);
    }

    public static void activateBoost(XamphurBoost boost) {
        activateBoost(boost, BOOST_HOURS);
    }

    public static void activateBoost(XamphurBoost boost, int hours) {
        activateBoost(null, boost, hours);
    }

    public static void activateBoost(@Nullable Player activator, XamphurBoost boost, int hours) {
        World.getWorldBoosts().stream()
                .filter(b -> b.getBoostType() == boost)
                .findAny()
                .ifPresentOrElse(
                        existingBoost -> existingBoost.extend(activator, 1, true),
                    () -> {
                        final long endTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
                        WorldBoost worldBoost = new WorldBoost(boost, endTime, hours);
                        worldBoost.activate(activator, true);
                    }
                );
    }

    public static void activateBoost(XamphurBoost boost, int hours, boolean announce) {
        long endTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
        WorldBoost worldBoost = new WorldBoost(boost, endTime, hours);
        worldBoost.activate(announce);
    }

    private void start() {
        WorldTasksManager.schedule(() -> World.spawnNPC(xamphur));
    }

    @Subscribe
    public static void onServerLaunch(ServerLaunchEvent event) {
        instance = new XamphurHandler();
        instance.start();
    }

    @SuppressWarnings("ConstantValue")
    public boolean isXamphurActive() {
        return xamphur != null && xamphur.fightStarted;
    }
}
