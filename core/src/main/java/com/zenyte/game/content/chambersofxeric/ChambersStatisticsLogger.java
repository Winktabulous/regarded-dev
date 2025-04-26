package com.zenyte.game.content.chambersofxeric;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidMap;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.content.chambersofxeric.rewards.RaidRewards;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.plugins.events.ServerShutdownEvent;
import com.zenyte.utils.StaticInitializer;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author Kris | 15/09/2019 11:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public class ChambersStatisticsLogger {
    private static final Logger log = LoggerFactory.getLogger(ChambersStatisticsLogger.class);

    @Subscribe
    public static final void onLaunch(final ServerLaunchEvent event) {
        instance.build();
    }

    @Subscribe
    public static final void onShutdown(final ServerShutdownEvent event) {
        instance.shutdown();
    }

    private static final ChambersStatisticsLogger instance = new ChambersStatisticsLogger();
    private static final int BUFFER_CAPACITY = 8192;
    private static final SimpleDateFormat loggerDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private transient Writer logWriter;
    private final transient Queue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private transient int bytes;
    private transient ForkJoinTask<?> task;
    private boolean closed;

    private final void build() {
        try {
            logWriter = new BufferedWriter(new FileWriter(new File("data/logs/chambers of xeric.log"), true));
        } catch (IOException e) {
            log.error("", e);
        }
    }

    private final void shutdown() {
        try {
            if (task == null || task.isDone()) {
                write();
            }
            //Wait for the last write process to end.
            task.get();
            closed = true;
            logWriter.flush();
            logWriter.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void log(final String message) {
        final String queuedMessage = String.format("%s - %s%s", loggerDateFormat.format(new Date()), message, System.lineSeparator());
        messageQueue.add(queuedMessage);
        bytes += queuedMessage.length();
        if (bytes >= BUFFER_CAPACITY) {
            if (task != null && !task.isDone()) {
                return;
            }
            write();
        }
    }

    private void write() {
        task = ForkJoinPool.commonPool().submit(() -> {
            try {
                if (closed) {
                    return;
                }
                String message;
                while ((message = messageQueue.poll()) != null) {
                    logWriter.write(message);
                    bytes -= message.length();
                }
                logWriter.flush();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    static final void record(@NotNull final Raid raid) {
        if (raid.isRecorded()) {
            return;
        }
        raid.setRecorded(true);
        final String message = instance.compose(raid);
        instance.log(message);
        if (GameConstants.WORLD_PROFILE.isDevelopment()) {
            instance.write();
        }
    }

    private String compose(@NotNull final Raid raid) {
        final StringBuilder builder = new StringBuilder();
        final RaidParty party = raid.getParty();
        if (party == null) {
            throw new IllegalStateException();
        }
        final ClanChannel channel = party.getChannel();
        if (channel == null) {
            throw new IllegalStateException();
        }
        builder.append("Chambers of Xeric: ").append(channel.getOwner()).append("'s channel \"").append(channel.getPrefix()).append("\"").append(System.lineSeparator());
        builder.append("\t").append("Mode: ").append(raid.isChallengeMode() ? "Challenge" : "Normal").append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("\t").append("Status: ").append(StringFormatUtil.formatString(raid.getStatus().toString())).append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("\t").append("Total points: ").append(raid.getTotalPoints()).append(System.lineSeparator()).append(System.lineSeparator());
        final Set<Player> players = raid.getPlayers();
        final Set<String> originalPlayers = raid.getOriginalPlayers();
        if (originalPlayers.size() > 0) {
            builder.append("\t").append("Players(").append(players.size()).append("/").append(originalPlayers.size()).append(")").append(System.lineSeparator());
            builder.append("\t\t");
            for (final Player player : players) {
                builder.append(player.getUsername()).append("(").append(raid.getPoints(player)).append(")").append(", ");
            }
            if (players.size() > 0) {
                builder.delete(builder.length() - 2, builder.length());
            }
            if (originalPlayers.size() > players.size()) {
                builder.append(System.lineSeparator()).append("\t").append("Players who have left: ").append(System.lineSeparator());
                builder.append("\t\t");
                for (final String originalPlayer : originalPlayers) {
                    final Player existingPlayer = CollectionUtils.findMatching(players, player -> player.getUsername().equals(originalPlayer));
                    if (existingPlayer != null) {
                        continue;
                    }
                    builder.append(originalPlayer).append(", ");
                }
                builder.delete(builder.length() - 2, builder.length());
            }
            builder.append(System.lineSeparator()).append(System.lineSeparator());
        }
        final RaidRewards rewards = raid.getRewards();
        if (rewards != null) {
            final Map<String, List<Item>> originalRewards = rewards.getOriginalRewards();
            if (!originalPlayers.isEmpty()) {
                builder.append("\t").append("Rewards").append(System.lineSeparator());
                for (final Map.Entry<String, List<Item>> entry : originalRewards.entrySet()) {
                    builder.append("\t\t").append(entry.getKey()).append(": ");
                    for (final Item item : entry.getValue()) {
                        builder.append(item.getName()).append(" x ").append(item.getAmount()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                    builder.append(System.lineSeparator());
                }
            }
            builder.append(System.lineSeparator());
        }
        final String onyxMessage = raid.getOnyxDropMessage();
        if (onyxMessage != null) {
            builder.append("\t").append("Onyx drop").append(System.lineSeparator());
            final String[] split = onyxMessage.split(", ");
            builder.append("\t\t").append("Username: ").append(split[0]).append(System.lineSeparator());
            builder.append("\t\t").append("Time: ").append(split[1]).append('(').append(new Date(Long.parseLong(split[1]))).append(')').append(System.lineSeparator());
            builder.append(System.lineSeparator());
        }
        final List<String> deaths = raid.getDeaths();
        if (!deaths.isEmpty()) {
            builder.append("\t").append("Deaths").append(System.lineSeparator());
            for (final String death : deaths) {
                builder.append("\t\t").append(death).append(System.lineSeparator());
            }
            builder.append(System.lineSeparator());
        }
        final Int2ObjectMap<Pair<String, Long>> floorDurations = raid.getLevelCompletionMessages();
        if (!floorDurations.isEmpty()) {
            builder.append("\t").append("Time on floor completion").append(System.lineSeparator());
            for (int i = 3; i >= 0; i--) {
                final Pair<String, Long> pair = floorDurations.get(i);
                if (pair == null) {
                    continue;
                }
                final Long millis = pair.getRight();
                final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
                final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
                final long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
                builder.append("\t\t").append(pair.getKey()).append(pair.getRight()).append('(').append(hours == 0 ? String.format("%02d:%02d", minutes, seconds) : String.format("%02d:%02d:%02d", hours, minutes, seconds)).append(')').append(System.lineSeparator());
            }
            builder.append(System.lineSeparator());
        }
        final RaidMap map = raid.getMap();
        if (map != null) {
            final ObjectArrayList<RaidArea> chunks = new ObjectArrayList<RaidArea>(map.getRaidChunks());
            chunks.add(raid.getMap().getBoss());
            builder.append("\tRooms").append(System.lineSeparator());
            for (final RaidArea room : chunks) {
                final RaidRoom type = room.getType();
                builder.append("\t\t");
                builder.append('[');
                builder.append(type == null ? 'O' : type.getTypeChar());
                builder.append(']');
                builder.append(type == null ? "Great Olm" : type.getFormattedName()).append(" - ").append(room.getIndex()).append(", ").append(room.getRotation()).append(", ").append(room.getChunkX()).append(", ").append(room.getChunkY()).append(", ").append(room.getToPlane());
                final long enterTime = room.getEnterTime();
                final long leaveTime = room.getLeaveTime();
                if (enterTime != 0) {
                    builder.append(" - time: ").append(enterTime).append("-").append(leaveTime == 0 ? "?" : leaveTime);
                    if (leaveTime != 0) {
                        final long ticks = leaveTime - enterTime;
                        final long seconds = TimeUnit.TICKS.toSeconds(ticks) % 60;
                        final long minutes = TimeUnit.TICKS.toMinutes(ticks);
                        builder.append('(').append(Utils.formatTime(minutes, seconds)).append(')');
                    }
                }
                builder.append(System.lineSeparator());
            }
        }
        builder.append(System.lineSeparator());
        builder.append("---End---");
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
