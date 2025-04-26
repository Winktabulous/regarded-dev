package com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.second.spawnCrabs
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell

/**
 * @author Jire
 */
internal object NylocasPassiveSpell : PassiveSpell {

    override val nextSpell = WebPassiveSpell

    override fun VerzikVitur.cast() = spawnCrabs()

}