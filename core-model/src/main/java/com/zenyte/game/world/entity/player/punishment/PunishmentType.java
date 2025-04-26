package com.zenyte.game.world.entity.player.punishment;

import com.zenyte.utils.Ordinal;

/**
 * @author Kris | 09/03/2019 19:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@Ordinal
public enum PunishmentType {
    YELL_MUTE(PunishmentCategory.YELL_MUTE, "Yell Mute"),
    IP_YELL_MUTE(PunishmentCategory.YELL_MUTE, "IP Yell Mute"),
    MUTE(PunishmentCategory.MUTE, "Mute"),
    IP_MUTE(PunishmentCategory.MUTE, "IP Mute"),
    BAN(PunishmentCategory.BAN, "Ban"),
    IP_BAN(PunishmentCategory.BAN, "IP Ban"),
    ;

    private final PunishmentCategory category;
    private final String formattedString;

    PunishmentType(PunishmentCategory category, String formattedString) {
        this.category = category;
        this.formattedString = formattedString;
    }

    public PunishmentCategory getCategory() {
        return category;
    }

    public String getFormattedString() {
        return formattedString;
    }

}
