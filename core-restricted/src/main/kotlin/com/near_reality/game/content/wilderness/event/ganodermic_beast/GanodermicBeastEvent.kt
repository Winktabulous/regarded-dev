package com.near_reality.game.content.wilderness.event.ganodermic_beast

import com.near_reality.game.content.wilderness.event.WildernessEvent
import com.near_reality.game.content.wilderness.event.ganodermic_beast.GanodermicBeast.SpawnLocation
import com.near_reality.game.util.formattedString
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import java.util.*
import kotlin.collections.ArrayDeque

object GanodermicBeastEvent : WildernessEvent {

    private val availableSpawnLocations = arrayOf(
        SpawnLocation("at the Rogues Castle", Location(3306, 3931)),
        SpawnLocation("at the Spider Hill", Location(3133, 3885)),
        SpawnLocation("at the Bone Yard", Location(3259, 3732)),
        SpawnLocation("at the 19 Portal", Location(3200, 3672)),
        SpawnLocation("south of Chaos Temple", Location(3237, 3569)),
        SpawnLocation("at the Fountain of Rune", Location(3320, 3885)),
        SpawnLocation("at the Bandit Camp", Location(3033, 3699))
    )
    private val spawnLocations = ArrayDeque<SpawnLocation>()
    private var spawnLocation = nextSpawnLocation()
    private lateinit var beast : GanodermicBeast

    override fun start(): Optional<String> {
        spawnLocation = nextSpawnLocation()
        beast = GanodermicBeast(spawnLocation)
        beast.spawn()
        return Optional.of(
            "The Ganodermic Beast's plan for world destruction is being put into motion! " +
                    "Rumours has it she is located ${spawnLocation.name}."
        )
    }

    override fun cancel() {
        if (this::beast.isInitialized)
            beast.finish()
    }

    override fun completed(): Boolean =
        this::beast.isInitialized && beast.isDead

    override fun stateDialogue(state: WildernessEvent.State): String {
        return when (state) {
            is WildernessEvent.State.Scheduled -> {
                val timeLeft = state.timeLeft
                buildString {
                    append("The Ganodermic Beast will spawn in")
                    append(timeLeft.formattedString)
                    append("!")
                }
            }
            is WildernessEvent.State.Active -> {
                "The Ganodermic Beast is currently active ${spawnLocation.name}."
            }
        }
    }

    override fun name(): String =
        "The Ganodermic Beast"

    override fun teleportLocation(): Optional<Location> {
        return if (this::beast.isInitialized)
            Optional.of(beast.middleLocation.copy().transform(Utils.random(5), Utils.random(5)))
        else
            Optional.empty()
    }

    private fun nextSpawnLocation(): SpawnLocation {
        if (spawnLocations.isEmpty()) {
            spawnLocations += availableSpawnLocations
            spawnLocations.shuffle()
        }
        return spawnLocations.removeFirst()
    }
}
