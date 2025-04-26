package com.near_reality.game.content.gauntlet.hunllef;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.HitType;

/**
 * @author Andys1814.
 * @since 1/21/2022.
 */
public enum HunllefType {
    MELEE_RESISTANT(9021, 9035, HitType.MELEE),
    RANGED_RESISTANT(9022, 9036, HitType.RANGED),
    MAGIC_RESISTANT(9023, 9037, HitType.MAGIC);

    private static final HunllefType[] TYPES = values();

    private final int npcId, corruptedNpcId;

    private final HitType resistance;

    HunllefType(int npcId, int corruptedNpcId, HitType resistance) {
        this.npcId = npcId;
        this.corruptedNpcId = corruptedNpcId;
        this.resistance = resistance;
    }

    public static final HunllefType random() {
        return Utils.random(TYPES);
    }

    public static HunllefType forHit(HitType hitType) {
        if (hitType == HitType.MELEE) return MELEE_RESISTANT;
        if (hitType == HitType.RANGED) return RANGED_RESISTANT;
        if (hitType == HitType.MAGIC) return MAGIC_RESISTANT;
        throw new IllegalArgumentException("Unsupported hit type encountered during Hunllef combat: " + hitType + ".");
    }

    public int getNpcId() {
        return npcId;
    }

    public int getCorruptedNpcId() {
        return corruptedNpcId;
    }

    public HitType getResistance() {
        return resistance;
    }
}
