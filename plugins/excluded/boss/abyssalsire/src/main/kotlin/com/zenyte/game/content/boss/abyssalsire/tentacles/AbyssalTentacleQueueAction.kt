package com.zenyte.game.content.boss.abyssalsire.tentacles

/**
 * @author Jire
 */
internal data class AbyssalTentacleQueueAction(
    val run: (() -> Unit)? = null,
    var blockTicks: Int = 0
)