package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

/**
 * @author Kris | 31/10/2018 13:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum DragonfireType {

    /** Chromatic dragons(green, blue, red, black) dragonfire. */
    CHROMATIC_DRAGONFIRE(1F, DragonfireProtection.defaultProtection),

    /** Metallic dragons, Basic dragonfire by KBD & Vorkath. */
    STRONG_DRAGONFIRE(1.25F, DragonfireProtection.defaultProtection),

    /** Wyvern icy breath. */
    WYVERN_ICY_DRAGONFIRE(0.95F, DragonfireProtection.wyvernProtection),

    /** Special dragonfire by King black dragon, Vorkath. */
    EXTREME_DRAGONFIRE(1.75F, DragonfireProtection.defaultProtection);

    DragonfireType(final float fullProtectionTier, final DragonfireProtection... acceptableProtections) {
        this.fullProtectionTier = fullProtectionTier;
        this.acceptableProtections = acceptableProtections;
    }

    final float fullProtectionTier;
    final DragonfireProtection[] acceptableProtections;

}
