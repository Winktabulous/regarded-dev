package com.zenyte.game.content.theatreofblood.room.verzikvitur.second

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasHagios
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasIschyros
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasToxobolos
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

internal fun VerzikVitur.spawnCrabs() {
    for (p in room.validTargets) {
        val crabLocation = findCrabLocation(p) ?: continue

        val crabToSpawnClass = Utils.random(crabsToSpawnClass)
        val crab = crabToSpawnClass.java.getDeclaredConstructor(*crabConstructorParams).newInstance(room, crabLocation)
        crab.spawn()
    }
}

private fun VerzikVitur.findCrabLocation(player: Player): Location? {
    val location = player.location.copy()
    val direction = Direction.getDirection(middleLocation, location)
    for (dist in 11 downTo 2) {
        val gen = location.transform(direction, dist)
        val routeResult = RouteFinder.findRoute(location, size, TileStrategy(gen), false)
        if (routeResult != RouteResult.ILLEGAL && routeResult.steps < 7)
            return gen
    }
    return null
}

private val crabsToSpawnClass = arrayOf(NylocasHagios::class, NylocasToxobolos::class, NylocasIschyros::class)
private val crabConstructorParams = arrayOf(VerzikViturRoom::class.java, Location::class.java)