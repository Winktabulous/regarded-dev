package com.zenyte.game.world.entity.player.loyalty;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.LogoutEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 29/04/2019 13:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LoyaltyManager {
    public static final int LOYALTY_POINTS_PER_INTERVAL = 10;
    public static final int LOYALTY_POINTS_INTERVAL_MINUTES = 30;
    public static final long LOYALTY_POINTS_INTERVAL_TICKS = (long) (TimeUnit.MINUTES.toMillis(LOYALTY_POINTS_INTERVAL_MINUTES) / GameConstants.TICK);
    /**
     * A linked list of sessions. This set will be cleansed of entries older than a month ago on logout.
     * List is defined as nullable because existing players will have forceset the variable to nullable due
     * to reflection loading.
     */
    @Nullable
    private final List<LoyaltySession> sessions = new LinkedList<>();
    /**
     * The player who owns this loyalty manager.
     */
    @NotNull
    private final transient Player player;
    /**
     * The date and time when the player logged into the game; only tracks this session. The date is not serialized
     * and will instead be added to the {@link LoyaltyManager#sessions} as a session.
     */
    @Nullable
    private transient Date login;

    public LoyaltyManager(@NotNull final Player player) {
        this.player = player;
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final LoyaltyManager currentLoyalty = player.getLoyaltyManager();
        currentLoyalty.login = Date.from(Instant.now());
        final Player saved = event.getSavedPlayer();
        final LoyaltyManager loyaltyManager = saved.getLoyaltyManager();
        if (loyaltyManager == null) {
            return;
        }
        if (loyaltyManager.sessions == null) {
            return;
        }
        Objects.requireNonNull(currentLoyalty.sessions).addAll(loyaltyManager.sessions);
    }

    @Subscribe
    public static final void onLogout(final LogoutEvent event) {
        final Player player = event.getPlayer();
        final LoyaltyManager loyalty = player.getLoyaltyManager();
        Objects.requireNonNull(loyalty.sessions).add(new LoyaltySession(loyalty.login, Date.from(Instant.now())));
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        final Date minimumDate = calendar.getTime();
        Objects.requireNonNull(loyalty.sessions).removeIf(session -> session.getLogout().before(minimumDate));
    }

    /**
     * Gets the consecutive number of days logged in between(and including) the two dates specifified. Starts counting from the end, if it runs into a day that hasn't been properly
     * fulfilled, breaks out of the counting process and returns the number of days that had consecutive logins.
     *
     * @param from                   the date from when to start counting the consecutive logins.
     * @param until                  the date until when to count the days.
     * @param requiredDurationPerDay the time spent online necessarily to count a day as a successful consecutive login day.
     * @param timeUnit               the time unit in which the aforementioned time is represented in.
     * @return the number of days that the player has consecutively logged in for.
     */
    public int getConsecutiveDaysLoggedIn(@NotNull final LocalDate from, @NotNull final LocalDate until, final int requiredDurationPerDay, @NotNull final TimeUnit timeUnit) {
        final long startDay = from.toEpochDay();
        final long endDay = until.toEpochDay();
        Preconditions.checkArgument(endDay > startDay, "End date cannot be after start date.");
        final long necessaryDuration = timeUnit.toMillis(requiredDurationPerDay);
        int count = 0;
        for (long epochDay = endDay; epochDay >= startDay; epochDay--) {
            final LocalDate date = LocalDate.ofEpochDay(epochDay);
            final long duration = getLoggedInDuration(date);
            if (duration < necessaryDuration) {
                break;
            }
            count++;
        }
        return count;
    }

    /**
     * Gets the duration the player has been in-game for during the requested day.
     *
     * @param day the day which to count.
     * @return the amount of milliseconds the player was online for during that day. All sessions are added together.
     */
    private long getLoggedInDuration(@NotNull final LocalDate day) {
        final Date date = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date endDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDate.setTime(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
        long totalMilliseconds = 0;
        for (final LoyaltySession session : Objects.requireNonNull(this.sessions)) {
            final Date login = session.getLogin();
            final Date logout = session.getLogout();
            if (logout.before(date) || login.after(endDate)) {
                continue;
            }
            final long start = (date.before(login) ? login : date).getTime();
            final long end = (logout.after(endDate) ? endDate : logout).getTime();
            final long difference = end - start;
            Preconditions.checkArgument(difference > 0);
            totalMilliseconds += difference;
        }
        return totalMilliseconds;
    }

    /**
     * Informs the player of their consecutive session timer; Every {@link LoyaltyManager#LOYALTY_POINTS_INTERVAL_MINUTES} minutes this method gets referenced and the player is given
     * {@link LoyaltyManager#LOYALTY_POINTS_PER_INTERVAL} loyalty points in return.
     *
     * @param count the number of intergals that has executed since the login, including this call. A count of two would imply 2 *
     *              {@link LoyaltyManager#LOYALTY_POINTS_INTERVAL_MINUTES} minutes of gameplay has occurred during this session.
     */
    public void informSession(final int count) {
        if (player.getTemporaryAttributes().get("User deemed inactive") != null) {
            return;
        }
        final int totalMinutes = count * LOYALTY_POINTS_INTERVAL_MINUTES;
        final int hours = totalMinutes / 60;
        final int remainingMinutes = totalMinutes % 60;
        final String time = hours == 0 ? remainingMinutes + " minutes" : (hours + (hours == 1 ? " hour " : " hours ") + (remainingMinutes != 0 ? "and " : "") + (remainingMinutes == 0 ? "" : (remainingMinutes + " minutes")));
        int points = (int) (LOYALTY_POINTS_PER_INTERVAL * multiplier(count));
        /*if (World.hasBoost(XamphurBoost.VOTE_LOYALTY_X2)) {
            points *= 2;
        }*/

        setLoyaltyPoints(getLoyaltyPoints() + points);
        player.sendMessage(Colour.RS_GREEN.wrap("You receive " + points + " loyalty points for " + time.trim() + " of consecutive playtime. You now have " + getLoyaltyPoints() + " loyalty points."));
    }

    /**
     * The multiplier for the logged in session duration.
     * @param iteration the number of {@link LoyaltyManager#LOYALTY_POINTS_INTERVAL_MINUTES} the player has been logged in for.
     * @return the multiplier - 1.5X every 2.5 hours, 3x every 5 hours & 5x every 10 hours.
     */
    private static final float multiplier(final int iteration) {
        if (iteration % 20 == 0) {
            return 5;
        }
        if (iteration % 10 == 0) {
            return 3;
        }
        if (iteration % 5 == 0) {
            return 1.5F;
        }
        return 1;
    }

    public int getLoyaltyPoints() {
        return player.getNumericAttribute("loyalty points").intValue();
    }

    public void setLoyaltyPoints(final int value) {
        player.addAttribute("loyalty points", value);
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Loyalty points"), "Loyalty points: <col=ffffff>" + player.getLoyaltyManager().getLoyaltyPoints() + "</col>"));
    }
}
