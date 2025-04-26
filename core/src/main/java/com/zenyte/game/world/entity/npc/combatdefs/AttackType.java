package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.world.entity.masks.HitType;

/**
 * @author Kris | 18/11/2018 02:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum AttackType {
    STAB(HitType.MELEE),
    SLASH(HitType.MELEE),
    CRUSH(HitType.MELEE),
    RANGED(HitType.RANGED),
    MAGIC(HitType.MAGIC),

    /**
     * Melee will redirect to either stab, slash or crush, depending on what it is defined as in the npc's definitions.
     */
    MELEE(HitType.MELEE);

    private HitType hitType;

    AttackType(HitType hitType) {
        this.hitType = hitType;
    }

    public HitType getHitType() {
        return hitType;
    }

    public boolean isMelee() {
        return equals(STAB) || equals(SLASH) || equals(CRUSH) || equals(MELEE);
    }

    public boolean isRanged() {
        return equals(RANGED);
    }

    public boolean isMagic() {
        return equals(MAGIC);
    }
}
