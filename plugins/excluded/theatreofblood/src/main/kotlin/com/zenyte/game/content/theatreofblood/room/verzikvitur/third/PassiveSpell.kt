package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur

/**
 * @author Jire
 */
internal interface PassiveSpell {

    val nextSpell: PassiveSpell

    fun VerzikVitur.cast()

}