package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.WhispererSpecial
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.Layout.generateSafePath
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.Layout.layoutOptionOne
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.Layout.layoutOptionTwo
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-24
 */
object ShadowLeachSpecialAttack : WhispererSpecial {

    private var layout = if (Random.nextBoolean()) layoutOptionOne else layoutOptionTwo
    private var primaryPath = Random.nextBoolean()

    var safeTiles: Int = 0
    private var realWorldLeeches: ArrayList<CorruptedSeed> = ArrayList()

    var safePath: ArrayList<Location> = ArrayList()
    var totalLeechLocations: ArrayList<Location> = ArrayList()
    var shadowRealmLeeches: ArrayList<CorruptedSeed> = ArrayList()

    override fun setup(whisperer: WhispererCombat, player: Player) {
        safePath = generateSafePath(whisperer, primaryPath, layout)
        safeTiles = safePath.size
        whisperer.radius = 0
        whisperer.setLocation(whisperer.spawnLocation)
        schedule(2) {
            whisperer faceDir North
            setupScene(whisperer)
        }
    }

    private fun spawnLeechRealWorld(centerTile: Location) {
        val leech = CorruptedSeed(CORRUPTED_SEED_REAL_WORLD, centerTile)
        totalLeechLocations.add(centerTile)
        World.spawnObject(leech)
        realWorldLeeches.add(leech)
    }

    private fun spawnLeechShadowRealm(centerTile: Location) {
        val leechId: Int = if (safePath.contains(centerTile)) CORRUPTED_SEED_LIGHT_GREEN else CORRUPTED_SEED_DARK_GREEN
        val leechPosition: Location = centerTile offset Pair(-64, 0)
        val leech = CorruptedSeed(leechId, leechPosition)
        World.spawnObject(leech)
        shadowRealmLeeches.add(leech)
    }

    fun removeShadowLeech(leech: CorruptedSeed) {
        shadowRealmLeeches.remove(leech)
    }
    // TODO: If one is removed from one realm, it should be removed from the other as well...
    fun removeRealWorldLeech(leech: CorruptedSeed) {
        realWorldLeeches.remove(leech)
    }

    fun removeLeech(player: Player, whisperer: WhispererCombat, gfxId: Int, leech: WorldObject, location: Location) {
        // GFX of exploding leech
        World.sendGraphics(Graphics(gfxId), leech.location)
        // Remove the world object
        World.removeObject(leech)
        if (leech is CorruptedSeed)
            when(leech.id) {
                CORRUPTED_SEED_LIGHT_GREEN, CORRUPTED_SEED_DARK_GREEN -> removeShadowLeech(leech)
                CORRUPTED_SEED_REAL_WORLD -> removeRealWorldLeech(leech)
            }

        totalLeechLocations.remove(location)

        // If we step on a not safe leech, deal some damage
        if (Objects.equals(CORRUPTED_SEED_DARK_GREEN, leech.id) ||
            (Objects.equals(CORRUPTED_SEED_REAL_WORLD, leech.id) && !safePath.contains(location)))
            player.applyHit(whisperer hit player damage Random.nextInt(15, 25))

        // check if all the dark green leeches are still here
        if (safePath.isEmpty()) {
            val totalDarkGreen = shadowRealmLeeches.size - safeTiles
            if (shadowRealmLeeches.size == totalDarkGreen) // if no darkGreen were touched
                player sanity 15 // Restore Sanity 15%
        }
    }

    private fun setupScene(whisperer: WhispererCombat) {
        // spawn leeches
        layout.map {
            spawnLeechRealWorld(whisperer.middleLocation offset it)
            spawnLeechShadowRealm(whisperer.middleLocation offset it)
        }
    }

    override fun execute(whisperer: WhispererCombat, target: Player) {
        // check if all the dark green leeches are still here
        if (safePath.isEmpty()) {
            val totalDarkGreen = shadowRealmLeeches.size - safeTiles
            if (shadowRealmLeeches.size == totalDarkGreen) // if no darkGreen were touched
                target sanity 15 // Restore Sanity 15%
        }
        // Time ran out, and there are still safe tiles to have cleared
        else {
            target.applyHit(whisperer hit target damage 75)
            target sanity -75
        }
        whisperer.state.usingSpecial = false
    }
}