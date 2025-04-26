package com.zenyte.game.content.killstreak;

import com.zenyte.game.world.entity.player.Player;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KillstreakLog {

    public static final int DELETE_ENTRIES_AFTER_MINUTES = 45;

    private List<KillstreakLogEntry> entries = new ArrayList<>();

    public KillstreakLog() {}

    public boolean entryExists(Player player) {
        return entryExists(player.getIP(), player.getUsername());
    }

    public boolean entryExists(String ip, String username) {
        clearLog();
        return entries.stream().anyMatch(e ->
                        Objects.equals(e.getIpAddress(), ip)
                        || Objects.equals(e.getUsernameKilled(), username));
    }

    public void addEntry(Player player) {
        entries.add(KillstreakLogEntry.createEntry(player));
    }

    /**
     * Clears the necessary logs
     */
    private void clearLog() {
        var iterator = entries.iterator();
        var toDeleteDate = LocalDateTime.now().minus(DELETE_ENTRIES_AFTER_MINUTES, ChronoUnit.MINUTES);

        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.getDateKilledAsDateTime().isBefore(toDeleteDate)) {
                iterator.remove();
            }
        }
    }

    public void initialize(final KillstreakLog log) {
        if (log != null && log.entries != null) {
            entries = log.entries;
        }
    }

    public int getCurrentStreak() {
        return entries.size();
    }


}
