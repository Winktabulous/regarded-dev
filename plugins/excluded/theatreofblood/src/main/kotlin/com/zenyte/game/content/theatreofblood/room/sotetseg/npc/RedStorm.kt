package com.zenyte.game.content.theatreofblood.room.sotetseg.npc

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.sotetseg.ShadowRealmRoom
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.CharacterLoop
import it.unimi.dsi.fastutil.objects.ObjectIterator
import it.unimi.dsi.fastutil.objects.ObjectSet

/**
 * @author Tommeh
 * @author Jire
 */
internal class RedStorm(room: ShadowRealmRoom, private val mazeTiles: ObjectSet<Location>, val tile: Location) :
    TheatreNPC<ShadowRealmRoom>(room, STORM_ID, tile) {

    private var iterator: ObjectIterator<Location> = mazeTiles.iterator()
    private var lastPath: Location? = null

    override fun processNPC() {
        super.processNPC()

        if (checkForPlayers()) {
            return
        }

        if (location.matches(transformCoord(mazeTiles.last()))) {
            // storm has reached the end of the path
            finish()
            return
        }

        if (lastPath == null || lastPath?.matches(location) == true) {
            val next = transformCoord(iterator.next())
            addWalkSteps(next.x, next.y, -1, false)
            lastPath = next
        }
    }

    override fun sendDeath() {

    }

    override fun isDead(): Boolean {
        return false
    }

    private fun checkForPlayers(): Boolean {
        var hitPlayer = false
        val nearbyPlayers = CharacterLoop.find(middleLocation, 0, Player::class.java) { true }
        if (nearbyPlayers.isNotEmpty()) {
            for (p in nearbyPlayers) {
                if (p != room.player) {
                    p.applyHit(Hit(Utils.random(35, 45), HitType.REGULAR))
                    hitPlayer = true
                }
            }
            resetWalkSteps()
            lastPath = null
        }

        return hitPlayer
    }

    companion object {

        fun transformCoord(loc: Location): Location {
            return loc.transform(-1, -1)
        }

        const val STORM_ID = 8389 // doesn't have a name so no NpcId constant :[

    }

}