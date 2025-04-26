package com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells

import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell

/**
 * @author Jire
 */
internal object DefaultPassiveSpell : PassiveSpell by NylocasPassiveSpell {
    override val nextSpell = NylocasPassiveSpell
}