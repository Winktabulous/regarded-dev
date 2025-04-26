package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit

/**
 * @author Jire
 */
internal abstract class TheatreBossNPC<T : TheatreRoom>(
    room: T,
    id: Int,
    tile: Location?,
    facing: Direction = Direction.DEFAULT
) : TheatreNPC<T>(room, id, tile, facing) {

    override fun heal(amount: Int) {
        super.heal(amount)

        room.refreshHealthBar()
    }

    override fun removeHitpoints(hit: Hit) {
        super.removeHitpoints(hit)

        room.refreshHealthBar()
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        room.complete()
    }

}