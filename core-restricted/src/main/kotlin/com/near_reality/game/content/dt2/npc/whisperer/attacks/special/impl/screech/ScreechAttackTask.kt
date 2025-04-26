package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech.ScreechSpecialAttack.pillars
import com.zenyte.game.task.TickTask
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.pathfinding.Flags
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-26
 */
class ScreechAttackTask(
    var whisperer: WhispererCombat,
    var target: Player
) : TickTask() {

    override fun run() {
        if (pillars.isEmpty()) return stop()
        val startingPoint = whisperer.spawnLocation offset Pair(0, -10)
        when(ticks++) {
            0 -> World.sendGraphics(Graphics(2454), startingPoint)
            1 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            2 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            3 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            4 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            5 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            6 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            7 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            8 -> updateSurroundingTilesByRing(startingPoint, ticks) { tile -> action(tile) }
            9 -> stop()
        }
    }

    private fun action(tile: Location) {
        run {
            checkTile(tile)
            tile.setLocation(tile.x + 1, tile.y + 1, 0)
        }
    }

    private fun checkTile(tile: Location) {
        // Check the Pillars first
        val iterator = pillars.iterator()
        while (iterator.hasNext()) {
            val pillar = iterator.next()
            if (Objects.equals(pillar.location, tile)) {
                pillar.applyHit(whisperer hit pillar damage 20)
                pillar spotanim 2462 delay 0

            }
            if (pillar.isDead || pillar.isFinished) {
                pillar.remove()
                iterator.remove()
                return
            }
        }
        // Then check the player
        if (Objects.equals(target.location, tile))
            if (!target.isDead && !target.isFinished) {
                target sanity -45
                target.applyHit(whisperer hit target damage 45)
                target spotanim 2462 delay 0
                return
            }
        World.sendGraphics(Graphics(2471), tile)
    }

    private fun updateSurroundingTilesByRing(tile: Location, radius: Int, updateFunction: (Location) -> Unit) {
        val rings = getSurroundingTilesByRing(tile, radius)
        for (ring in rings)
            for (surroundingTile in ring)
                updateFunction(surroundingTile)
    }

    private fun getSurroundingTilesByRing(tile: Location, radius: Int): List<List<Location>> {
        val rings = mutableListOf<List<Location>>()

        for (r in 1..radius) {
            val ring = mutableListOf<Location>()
            for (dx in -r..r) {
                for (dy in -r..r) {
                    if (kotlin.math.abs(dx) == r || kotlin.math.abs(dy) == r) {
                        if ((World.getMask(tile) and Flags.FLOOR_DECORATION) == 0 && World.getObjectWithType(tile, 10) == null)
                            if (dx != 0 || dy != 0)
                                ring.add(Location(tile.x + dx, tile.y + dy))
                    }
                }
            }
            rings.add(ring)
        }
        return rings
    }
}