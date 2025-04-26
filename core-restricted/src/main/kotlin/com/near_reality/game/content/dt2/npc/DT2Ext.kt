package com.near_reality.game.content.dt2.npc

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.offset
import com.near_reality.game.world.Boundary
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.game.world.entity.player.Player
import java.util.*

fun Player.getOffsetLocationInArena(instance: VardorvisInstance) : Optional<Location> {
    val boundary = Boundary(instance.getLocation(1124, 3423), instance.getLocation(1134, 3413))
    var potentialLocation = this.location.transform(Utils.random(4), Utils.random(4), 0)
    var searchFinderLimiter = 0
    while(!boundary.`in`(potentialLocation)) {
        potentialLocation = this.location.transform(Utils.random(4), Utils.random(4), 0)
        searchFinderLimiter++
        if(searchFinderLimiter == 20) {
            this.sendDeveloperMessage("Unable to find spawn loc for Vardorvis head.")
            log(LogLevel.ERROR, "Unable to find spawn loc for Vardorvis head.")
            return Optional.empty()
        }
    }
    return Optional.of(potentialLocation)
}

fun Player.getPotentialSpawnsInArena(instance: VardorvisInstance, radius: Int, count: Int) : List<Location> {
    val potentialSpawns = mutableListOf<Location>()
    val boundary = Boundary(instance.getLocation(1124, 3423), instance.getLocation(1134, 3413))
    var searchFinderLimiter = 0
    while(searchFinderLimiter < radius * radius) {
        val potentialLocation = (this.location.transform(Utils.random(radius), Utils.random(radius), 0)) offset Pair(-(radius/2), -(radius/2))
        if(boundary.`in`(potentialLocation))
            potentialSpawns.add(potentialLocation)
        searchFinderLimiter++
    }
    return potentialSpawns.shuffled().take(count)
}

var Player.entangled by attribute("dt2_entangled", false)
var Player.entangledFreedomCounter by attribute("dt2_entangledFreedomCounter", 0)