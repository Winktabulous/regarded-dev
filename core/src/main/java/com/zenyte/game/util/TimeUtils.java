package com.zenyte.game.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-18
 */
public class TimeUtils {
    public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    public static String addTimestamp(String message) {
        return "[" + dateFormat.format(Calendar.getInstance().getTime()) + "]: " + message;
    }

    public static String fromMs(long ms, boolean seconds) {
        long days = TimeUnit.MILLISECONDS.toDays(ms);
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        if(days > 0) {
            if(seconds) {
                return String.format(
                    "%02dd %02dh %02dm %02ds",
                    days,
                    hours - TimeUnit.DAYS.toHours(days),
                    minutes - TimeUnit.HOURS.toMinutes(hours),
                    TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes)
                );
            } else {
                return String.format(
                    "%02dd %02dh %02dm",
                    days,
                    hours - TimeUnit.DAYS.toHours(days),
                    minutes - TimeUnit.HOURS.toMinutes(hours)
                );
            }
        } else {
            if(seconds) {
                return String.format(
                    "%02dh %02dm %02ds",
                    hours,
                    minutes - TimeUnit.HOURS.toMinutes(hours),
                    TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes)
                );
            } else {
                return String.format(
                    "%02dh %02dm",
                    hours,
                    minutes - TimeUnit.HOURS.toMinutes(hours)
                );
            }
        }
    }

    public static long getDaysToMillis(long days) {
        return days * getHoursToMillis(24L);
    }
    public static long getHoursToMillis(long hours) {
        return hours * getMinutesToMillis(60L);
    }

    public static long getMinutesToMillis(long minutes) {
        return minutes * 60000L;
    }

}
