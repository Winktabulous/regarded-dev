package com.zenyte.game.records;

import mgi.utilities.ColorUtils;

import java.time.LocalDate;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-20
 */
public record SpeedRecord(
    String eventName,
    String recordHolder,
    LocalDate recordTime,
    Long recordedSpeed,
    Boolean isBoss) {

    public String toString() {
        long minutes = recordedSpeed / 60;
        long seconds = recordedSpeed % 60;
        return "%s <col=305DFF>has just set a new server record by %s %s in</col> <col=ff0000>%d:%s%d</col>"
            .formatted(
                recordHolder(),
                isBoss() ? "defeating" : "Running",
                eventName(),
                minutes,
                seconds < 10 ? "0" : "",
                seconds
            );
    }
}
