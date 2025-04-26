package com.zenyte.game.world.entity.masks;

import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 28. march 2018 : 0:37.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * <p>
 * Mark id must be a positive integer!
 */
public enum HitType {
    MISSED(12, 13),
    //Regular does not invoke special effects such as vengeance.
    REGULAR(16, 17),
    //Used for damage not applied by any direct combat (thieving)
    TYPELESS(16, 17),
    //Default invokes special effects such as vengeance.
    DEFAULT(16, 17),
    MELEE(16, 17),
    MAGIC(16, 17),
    RANGED(16, 17),
    POISON(2),
    YELLOW(3),
    DISEASED(4),
    VENOM(5),
    HEALED(6),
    SHIELD_CHARGE(11),
    PALM_LOWER(15),

    SHIELD(18, 19),
    ARMOUR(20, 21),
    CHARGE(22, 23),
    DISCHARGE(24, 25),
    CORRUPTION(0),
    SHIELD_DOWN(60),
    WARDENS(53, 54),
    PRAYER_DRAIN(60),
    BLEED(67),
    SANITY_DRAIN(71),
    SANITY_RESTORE(72),
    DOOM(73),
    BURN(74)
    ;

    private final int id;
    private final int dynamicID;

    public static final HitType[] values = values();

    HitType(int id, int dynamicID) {
        this.id = id;
        this.dynamicID = dynamicID;
    }

    HitType(int id) {
        this(id, id);
    }

    public void writeMask(RSBuffer buffer, boolean useDynamic) {
        buffer.writeSmart(useDynamic ? dynamicID : id);
    }

}
