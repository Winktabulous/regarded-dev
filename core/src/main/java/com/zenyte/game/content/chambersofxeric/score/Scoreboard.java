package com.zenyte.game.content.chambersofxeric.score;

import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 17. mai 2018 : 18:47:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Scoreboard extends Interface implements ScheduledExternalizable {
    private static final Logger log = LoggerFactory.getLogger(Scoreboard.class);
    /**
     * The list of the scores active on this world.
     */
    private static final List<Score[]> scores = new ObjectArrayList<>(13);
    /**
     * The target times for every bracket in ticks. The raid must be completed in under this time to be eligible for a score.
     */
    private static final int[] targetTimes = new int[] {7000, 6500, 5000, 4500, 4200, 4200, 4200, 4200, 4200, 4200, 4500, 6000, 8000};

    /**
     * Gets the target time for the specified size raid in ticks, under which the raid must be completed for it to be eligible for a score.
     *
     * @param size the size of the raid.
     * @return the target time in ticks under which the raid must be completed.
     */
    public static final int getTargetTime(final int size) {
        assert size >= 1 && size <= 100;
        final int slot = getGroupSlot(size);
        assert slot >= 0 && slot < targetTimes.length;
        return targetTimes[slot];
    }

    /**
     * Refreshes the scoreboard clientscript with the data from the tracked scores on this world.
     *
     * @param player the player for whom to update the clientscript.
     * @param slot   the slot of the bracket which to update.
     */
    private static final void refresh(@NotNull final Player player, final int slot) {
        if (slot < 0 || slot > scores.size() - 1) {
            return;
        }
        final Score[] scores = Scoreboard.scores.get(slot);
        final Object[] payload = new Object[8];
        payload[0] = scores[0] == null ? 0 : scores[0].getDuration();
        payload[1] = targetTimes[slot];
        int index = 2;
        final StringBuilder builder = new StringBuilder();
        for (final Score score : scores) {
            payload[index++] = score == null ? 0 : score.getDuration();
            builder.append(score == null ? "" : score.getClan()).append("|");
        }
        builder.delete(builder.length() - 1, builder.length());
        payload[payload.length - 1] = builder.toString();
        player.getPacketDispatcher().sendClientScript(2292, payload);
    }

    /**
     * Gets the group/bracket in which the specified size raid falls in.
     *
     * @param size the size of the raid.
     * @return the index of the bracket in which the raid falls.
     */
    private static final int getGroupSlot(final int size) {
        if (size <= 10) {
            return size - 1;
        }
        if (size <= 15) {
            return 10;
        }
        if (size < 23) {
            return 11;
        }
        return 12;
    }

    /**
     * Adds a score for the specified clan channel for the requested duration, as long as the raid is in challenge mode.
     *
     * @param clan     the clan for which to add the score.
     * @param duration the duration of the raid.
     */
    public static final void addScore(@NotNull final ClanChannel clan, final int duration) {
        final RaidParty party = clan.getRaidParty();
        if (party == null) {
            return;
        }
        final Raid raid = party.getRaid();
        if (raid == null || !raid.isChallengeMode()) {
            return;
        }
        if(raid.usingFakeScale)
            return;
        final Set<String> originalPlayers = raid.getOriginalPlayers();
        if (originalPlayers == null || originalPlayers.size() == 0) {
            return;
        }
        final int slot = getGroupSlot(originalPlayers.size());
        if (duration > targetTimes[slot]) {
            return;
        }
        Score[] scores = Scoreboard.scores.get(slot);
        if (scores == null) {
            scores = new Score[5];
        }
        final Score clanScore = new Score(clan.getPrefix(), duration);
        final ArrayList<Score> list = new ArrayList<Score>(Arrays.asList(scores));
        for (int i = scores.length - 1; i >= 0; i--) {
            final Score score = scores[i];
            if (score == null || score.getDuration() > duration) {
                continue;
            }
            list.add(i, clanScore);
        }
        if (!list.contains(clanScore)) {
            list.add(0, clanScore);
        }
        for (int i = 0; i < 5; i++) {
            scores[i] = i >= list.size() ? null : list.get(i);
        }
        Arrays.sort(scores, (s1, s2) -> s1 == null || s2 == null ? 1 : (Integer.compare(s1.getDuration(), s2.getDuration())));
        Scoreboard.scores.set(slot, scores);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 5;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        final Score[][] scores = getGSON().fromJson(reader, Score[][].class);
        Scoreboard.scores.addAll(Arrays.asList(scores));
        for (Score[] score : Scoreboard.scores) {
            Arrays.sort(score, (s1, s2) -> s1 == null || s2 == null ? 1 : (Integer.compare(s1.getDuration(), s2.getDuration())));
        }
        while (Scoreboard.scores.size() < 13) {
            Scoreboard.scores.add(new Score[5]);
        }
    }

    @Override
    public void write() {
        try {
            final PrintWriter pw = new PrintWriter(path(), StandardCharsets.UTF_8);
            pw.println(getGSON().toJson(scores.toArray(new Score[0][0])));
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @Override
    public String path() {
        return "data/raids/scores.json";
    }

    @Override
    protected void attach() {
        put(1, "Category");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Category"), 1, 13, AccessMask.CLICK_OP1);
        refresh(player, 0);
    }

    @Override
    protected void build() {
        bind("Category", (player, slotId, itemId, option) -> refresh(player, slotId - 1));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAIDS_SCOREBOARD;
    }
}
