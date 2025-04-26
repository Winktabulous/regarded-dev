package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.pathfinding.RouteFinder
import com.zenyte.game.world.entity.pathfinding.RouteResult
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnTornados() {
    attackSpeed = 5
    setForceTalk("I'm not done with you just yet!")
    for (p in room.validTargets) {
        val tornadoLocation = findTornadoLocation(p) ?: continue
        PurpleTornado(room, tornadoLocation).run {
            chaseTarget = p
            spawn()
        }
    }
}

internal fun VerzikVitur.findTornadoLocation(p: Player): Location? {
    val pLocation = p.location
    for (i in 0..32) {
        val gen = pLocation.transform(Direction.randomDirection(), Utils.random(4, 5))
        val routeResult = RouteFinder.findRoute(location, size, TileStrategy(gen), false)
        if (routeResult != RouteResult.ILLEGAL && routeResult.steps < 7)
            return gen
    }
    return null
}