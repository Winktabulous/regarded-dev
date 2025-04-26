package com.zenyte.plugins.dialogue;

import mgi.types.config.enums.Enums;

/**
 * @author Kris | 08/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum MakeType {
    MAKE(0, 1),
    MAKE_SETS(2, 3),
    BUY(4),
    BUY_SETS(5),
    COOK(6, 8),
    FIRE(9),
    STRING(10),
    CHARGE(11),
    CUT(12),
    SMELT(13),
    USE(14),
    UPGRADE(15),
    SPIN(16),
    TAKE(17),
    REQUEST(18),
    ENCHANT(19);

    MakeType(final int id) {
        this(id, id);
    }

    MakeType(final int id, final int maxSizeTenId) {
        this.id = id;
        this.maxSizeTenId = maxSizeTenId;
    }

    private final int id;
    private final int maxSizeTenId;

    public String toString() {
        return Enums.SKILL_DIALOGUE_STRING.getValue(id).orElseThrow(Enums.exception());
    }

    public int getId() {
        return id;
    }

    public int getMaxSizeTenId() {
        return maxSizeTenId;
    }

}
