package com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.HitType

/**
 * @author Jire
 */
internal class NylocasHagios(room: VerzikViturRoom, location: Location) : NylocasChaseCrab(room, ID, location) {

    override val styleToAttack = HitType.MAGIC

    companion object {
        const val ID = 8353
    }

}