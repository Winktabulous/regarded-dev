package com.near_reality.game.content.elven.area

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RSPolygon

/**
 * Trahaearn mine is a mine located in [Prifddinas].
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class TrahaearnMine : Prifddinas() {

    override fun enter(player: Player?) = Unit

    override fun leave(player: Player?, logout: Boolean) = Unit

    override fun name() = "Trahaearn mine"

    override fun polygons() = arrayOf(RSPolygon(3279, 12432, 3311, 12470))
}
