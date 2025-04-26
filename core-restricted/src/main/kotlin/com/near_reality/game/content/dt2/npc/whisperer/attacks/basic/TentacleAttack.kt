package com.near_reality.game.content.dt2.npc.whisperer.attacks.basic

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.WhispyUtils.rand
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import java.util.Objects

class TentacleAttack : WhispererBasicAttack {

    private val tentacles: MutableList<NPC> = mutableListOf()
    private val splashBig: Int = 2450
    private val splashSmall: Int = 2447
    private val shadowTentacle: Int = 12208

    private fun spawnPlusFormation(target: Location): MutableList<NPC> {
        return arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
            .map {
                NPC(shadowTentacle, target.transform(it, 4).transform(-1, -1, 0), false).apply {
                    spawnDirection = it.getCounterClockwiseDirection(4)
                }
            }
            .mapTo(tentacles) {
                it.radius = 0
                it.spawn()
            }
    }

    private fun spawnXFormation(target: Location): MutableList<NPC> {
        return arrayOf(Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST)
            .map {
                NPC(shadowTentacle, target.transform(it, 4).transform(-1, -1, 0), false).apply {
                    spawnDirection = it.getCounterClockwiseDirection(4)
                }
            }
            .mapTo(tentacles) {
                it.radius = 0
                it.spawn()
            }
    }

    override fun invoke(whisperer: WhispererCombat, target: AbstractEntity) {
        val targetLocation = Location(target.location)
        if (rand(1) == 0)
            spawnPlusFormation(targetLocation)
        else
            spawnXFormation(targetLocation)
        // Tentacle Smash
        tentacles.forEach { tentacle ->
            tentacle.faceLocation = targetLocation
            tentacle seq 10266
            World.sendGraphics(Graphics(splashBig), tentacle.middleLocation)
        }
        tentacles.forEach { tentacle -> splash(tentacle, 1) }
        tentacles.forEach { tentacle -> splash(tentacle, 2) }
        // Wave Effect
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                when (ticks++) {
                    0 -> tentacles.forEach { tentacle -> splash(tentacle, 3) }
                    1 -> {
                        World.sendGraphics(Graphics(splashBig), targetLocation)
                        if (Objects.equals(target.location, targetLocation)) {
                            if (!target.isDead && !target.isFinished) {
                                if (target is Player)
                                    target sanity -20
                                target.applyHit(whisperer hit target damage 20)
                                target spotanim splashBig delay 0
                            }
                        }
                        tentacles.forEach { it.remove() }
                        tentacles.clear()
                        stop()
                    }
                }
            }
        }, 0, 0)
    }

    private fun splash(tentacle: NPC, tick: Int) {
        val tentacleTile = tentacle.middleLocation
        val moveDir = tentacle.spawnDirection
        val toTile = tentacleTile.transform(moveDir, tick)
        World.sendGraphics(Graphics(splashSmall), toTile)
    }

}
