package com.zenyte.game.content.boss.abyssalsire

/**
 * @author Kris
 * @author Jire
 */
internal enum class AbyssalSirePhase(val animates: Boolean) {

    ASLEEP(false),
    AWAKE(true),
    MELEE(true),
    PREPARING_EXPLOSION(false),
    POST_EXPLOSION(false)

}