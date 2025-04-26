package com.near_reality.game.world

import com.zenyte.game.content.skills.slayer.Assignment
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.Player

/**
 * Represents an event involving a player.
 */
sealed interface PlayerEvent : WorldEvent {

    /**
     * The player subject to this event.
     */
    val player : Player

    /**
     * Posted before [com.zenyte.game.world.entity.player.Player.processEntity].
     */
    class Process(override val player: Player) : PlayerEvent

    /**
     * Posted before [com.zenyte.game.world.entity.player.Player.postProcess].
     */
    class PostProcess(override val player: Player) : PlayerEvent

    /**
     * Posted before [com.zenyte.game.world.entity.player.Player.processEntityUpdate].
     */
    class Update(override val player: Player) : PlayerEvent

    class Died(override val player: Player, val killer: Entity?) : PlayerEvent

    class SlayerTaskCompleted(override val player: Player, val assignment: Assignment) : PlayerEvent

    class ExperienceGained(override val player: Player, val skill: Int, val baseExperience: Double, val finalExperience: Double) : PlayerEvent

    class DamageDealt(override val player: Player, val target: Entity, val damage: Int) : PlayerEvent

}
