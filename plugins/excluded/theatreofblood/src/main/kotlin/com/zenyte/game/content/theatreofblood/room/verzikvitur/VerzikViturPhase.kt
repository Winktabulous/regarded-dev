package com.zenyte.game.content.theatreofblood.room.verzikvitur

/**
 * @author Jire
 */
internal enum class VerzikViturPhase(val npcID: Int) {

    NONE(8369),

    FIRST(8370),
    SECOND(8372),
    THIRD(8374);

    companion object {

        val values = values()

    }

}