package com.zenyte.game.world.entity.player.punishments;

import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.punishment.PunishmentCategory;
import com.zenyte.game.world.entity.player.punishment.PunishmentType;
import mgi.utilities.StringFormatUtil;

import java.time.Instant;
import java.util.Date;

/**
 * @author Kris | 09/03/2019 19:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Punishment {
    private final PunishmentType type;
    private final String reporter;
    private final String offender;
    private final String ip;
    private final Date timeOfPunishment;
    private final int durationInHours;
    private final Date expirationDate;
    private final String reason;

    boolean isExpired() {
        return expirationDate != null && expirationDate.before(Date.from(Instant.now()));
    }

    @Override
    public String toString() {
        return Colour.RS_GREEN.wrap(type.getFormattedString()) + " by " + Colour.RS_GREEN.wrap(StringFormatUtil.formatString(reporter)) + " expires " + Colour.RS_GREEN.wrap((expirationDate == null ? "Never" : expirationDate.toString()));
    }

    public String toLoginString() {
        final String formattedString = type.getFormattedString();
        return Colour.RS_GREEN.wrap(formattedString + (type.getCategory() == PunishmentCategory.MUTE ? "d" : "ned")) + " by " + Colour.RS_GREEN.wrap(StringFormatUtil.formatString(reporter)) + " - expires " + Colour.RS_GREEN.wrap((expirationDate == null ? "Never" : expirationDate.toString()));
    }

    public Punishment(PunishmentType type, String reporter, String offender, String ip, Date timeOfPunishment, int durationInHours, Date expirationDate, String reason) {
        this.type = type;
        this.reporter = reporter;
        this.offender = offender;
        this.ip = ip;
        this.timeOfPunishment = timeOfPunishment;
        this.durationInHours = durationInHours;
        this.expirationDate = expirationDate;
        this.reason = reason;
    }

    public PunishmentType getType() {
        return type;
    }

    public String getReporter() {
        return reporter;
    }

    public String getOffender() {
        return offender;
    }

    public String getIp() {
        return ip;
    }

    public Date getTimeOfPunishment() {
        return timeOfPunishment;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getReason() {
        return reason;
    }
}
