package com.zenyte.game.content.minigame.inferno.model;

import com.google.common.reflect.TypeToken;
import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.*;

/**
 * @author Tommeh | 15/12/2019 | 21:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class InfernoCompletions implements ScheduledExternalizable {

    private static final Logger log = LoggerFactory.getLogger(InfernoCompletions.class);

    public static Map<Object, List<String>> info = new HashMap<>();

    public static void add(final Player player) {
        info.get("completions_" + player.getGameMode()).add(player.getName());
    }

    public static void addPracticeRun(@NotNull final Player player) {
        List<String> list = info.computeIfAbsent("practice mode runs", k -> new ArrayList<>());
        list.add(new Date() + ": " + player.getUsername());
    }

    public static void setBroadcasted(final Player player) {
        final List<String> modeCompletions = info.get("broadcasted_" + player.getGameMode());
        if (!modeCompletions.contains(player.getName())) {
            modeCompletions.add(player.getName());
        }
    }

    public static boolean isBroadcasted(final Player player) {
        return info.get("broadcasted_" + player.getGameMode()).contains(player.getName());
    }

    public static int getCompletions(final GameMode mode) {
        return info.get("completions_" + mode).size();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 10;
    }

    @Override
    public void read(@NotNull BufferedReader reader) {
        info = getGSON().fromJson(reader, new TypeToken<Map<Object, List<String>>>() {
        }.getType());
        if (info.isEmpty()) {
            setDefaults();
        }
    }

    @Override
    public void write() {
        if (info.isEmpty()) {
            setDefaults();
        }
        out(getGSON().toJson(info));
    }

    private static void setDefaults() {
        for (final GameMode mode : GameMode.values) {
            info.put("completions_" + mode, new ArrayList<>());
            info.put("broadcasted_" + mode, new ArrayList<>());
        }
    }

    @Override
    public String path() {
        return "data/inferno completions.json";
    }
}
