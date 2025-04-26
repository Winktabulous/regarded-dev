package com.near_reality.game.content.wilderness.event.chest

import com.near_reality.game.content.wilderness.event.WildernessEvent
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * The wilderness chest event.
 *
 * @author Stan van der Bend
 */
object WildernessChestEvent : WildernessEvent {

    /**
     * The top of the rogue's castle.
     */
    private val CHEST_SPAWN_LOCATION = Location(3278, 3936, 3)
    private var chest: WildernessChestObject? = null

    fun pickupChest(player: Player) {
        removeChest()

    }

    override fun start(): Optional<String> {
        chest = WildernessChestObject(CHEST_SPAWN_LOCATION)
        World.spawnObject(chest)
        return Optional.of("A chest has spawned at the top of Rogue's castle.")
    }

    override fun cancel() {
        removeChest()
    }

    private fun removeChest() {
        chest?.let(World::removeObject)
        chest = null
    }

    override fun completed(): Boolean {
        return false
    }

    override fun stateDialogue(state: WildernessEvent.State): String {
        return when (state) {
            is WildernessEvent.State.Scheduled -> "<img=68> A chest will spawn in ${state.startTick} ticks at the top of Rogue's castle."
            is WildernessEvent.State.Active -> "<img=68> A chest has spawned at the top of Rogue's castle."
        }
    }
}
