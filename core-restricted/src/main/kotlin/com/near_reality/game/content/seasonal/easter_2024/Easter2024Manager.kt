package com.near_reality.game.content.seasonal.easter_2024

import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.Location
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory

/**
 * Handles the easter event mechanics.
 *
 * @author Stan van der Bend
 */
internal object Easter2024Manager {

    private lateinit var chicken: ColossalChoccoChicken
    private var waitingTask: WorldTask? = null
    private var chickenSpawningTask : WorldTask? = null
    private var chickenDiedTask : WorldTask? = null
    var enabled = false
    var event_ending = true

    private val logger = LoggerFactory.getLogger(Easter2024Manager::class.java)

    lateinit var state : Easter2024State
        private set

    fun spawnJaseNpc() {
        if(!enabled)
            return
        Easter2024JaseNPC(home = true, location = Location(3099, 3499, 0)).spawn()
        Easter2024JaseNPC(home = false, location = Location(1905, 5138, 2)).spawn()
    }

    private fun spawnChicken() {
        if(!enabled)
            return
        chicken = ColossalChoccoChicken()
        chicken.spawn()
        WorldBroadcasts.sendMessage("The Colossal Chicken has spawned, defeat her at ::easter!", BroadcastType.COLOSSAL_CHICKEN, true)
    }

    fun changeState(newState: Easter2024State) {
        waitingTask?.run(WorldTasksManager::stop)
        chickenSpawningTask?.run(WorldTasksManager::stop)
        chickenDiedTask?.run(WorldTasksManager::stop)
        if(!enabled || event_ending) {
            if(event_ending)
                state = newState
            return
        }
        when(newState) {
            Easter2024State.WAITING -> {
                val waitingTask = WorldTask {
                    changeState(Easter2024State.CHICKEN_SPAWNING)
                }
                Easter2024Manager.waitingTask = waitingTask
                WorldTasksManager.schedule(TimeUnit.HOURS.toTicks(1L).toInt(), task = waitingTask)
            }
            Easter2024State.CHICKEN_SPAWNING -> {
                val spawningTask = WorldTask {
                    changeState(Easter2024State.CHICKEN_SPAWNED)
                }
                chickenSpawningTask = spawningTask
                WorldBroadcasts.sendMessage("The Colossal Chicken will spawn soon, defeat her at ::easter!", BroadcastType.COLOSSAL_CHICKEN, true)
                WorldTasksManager.schedule(TimeUnit.SECONDS.toTicks(20L).toInt(), task = spawningTask)
            }
            Easter2024State.CHICKEN_SPAWNED -> {
                if (state != Easter2024State.CHICKEN_SPAWNING) {
                    logger.info("Cannot spawn chicken while not in waiting state.")
                    return
                }
                spawnChicken()
            }
            Easter2024State.CHICKEN_KILLED -> {
                if (state != Easter2024State.CHICKEN_SPAWNED) {
                    logger.info("Cannot start chicken death task while not in chicken spawned state.")
                    return
                }
                val deathTask = WorldTask {
                    changeState(Easter2024State.WAITING)
                }
                chickenDiedTask = deathTask
                WorldTasksManager.schedule(TimeUnit.SECONDS.toTicks(20L).toInt(), task = deathTask)
            }
        }
        state = newState
    }

    val chickenOrNull : ColossalChoccoChicken?
        get() = if (state == Easter2024State.CHICKEN_SPAWNED) chicken else null

    val chickenTypeOrNull : ChickenType?
        get() = chickenOrNull?.type
}
