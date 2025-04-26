package com.near_reality.game.content.elven.area

import com.zenyte.game.content.skills.hunter.node.Impling
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.Tirannwn
import com.zenyte.utils.TimeUnit

/**
 * Prifddinas is the city of the elves and the capital city of [Tirannwn].
 * Located just north of Isafdar, the city is the oldest surviving settlement on Gielinor,
 * being created in the First Age.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
open class Prifddinas : Tirannwn() {

    init {
        spawnImpling()
    }

    override fun enter(player: Player?) = Unit

    override fun leave(player: Player?, logout: Boolean) = Unit

    override fun name() = "Prifddinas"

    override fun polygons() = arrayOf(
        RSPolygon(12895), RSPolygon(13151),
        RSPolygon(12894), RSPolygon(13150),
    )

    private companion object {

        val locations = arrayOf(
            Location(3236, 6109, 0),
            Location(3295, 6109, 0),
            Location(3292, 6053, 0),
            Location(3234, 6052, 0),
        )

        val respawnTicks = TimeUnit.MINUTES.toTicks(30L).toInt()

        fun spawnImpling() {
            ImplingNPC(Impling.CRYSTAL.npcId, locations.random(), Direction.SOUTH, 20)
                .setOnFinished { WorldTasksManager.schedule(::spawnImpling, respawnTicks) }
                .spawn()
        }
    }
}
