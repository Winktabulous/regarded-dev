package com.near_reality.game.content.wilderness.event.hot_zone

import com.near_reality.game.content.wilderness.event.WildernessEvent
import com.near_reality.game.content.wilderness.event.WildernessEventSource
import com.near_reality.game.util.WorldTimer
import com.near_reality.game.util.formattedString
import com.near_reality.util.collection.refillPoolOf
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * The wilderness hot zone [event][WildernessEvent].
 *
 * @author Stan van der Bend
 */
object WildernessHotZoneEvent : WildernessEvent {

    internal val rewardHandler = WildernessHotZoneRewardHandler()

    private val areaPool = refillPoolOf(LowLevelWildernessHotZoneArea, HighLevelWildernessHotZoneArea)
    private var activeArea: WildernessHotZoneArea? = null
    private var endTimer = WorldTimer(5.minutes)

    fun inHotZone(player: Player) =
        !completed() && activeArea?.contains(player) ?: false

    internal fun messagePlayersInHotZone(message: String) {
        World.getPlayers().forEach {
            if (it != null && !it.isNulled && inHotZone(it))
                message(it, message)
        }
    }

    internal fun message(player: Player, message: String) {
        player.sendMessage("<img=68> ${Colour.RS_RED.wrap(message)}")
    }

    override fun name(): String =
        "Wilderness Hot Zone"

    override fun start(): Optional<String> {
        val nextArea = areaPool.poll()?:return Optional.empty()
        activeArea = nextArea
        endTimer.start()
        rewardHandler.reset()
        messagePlayersInHotZone("You are currently in the wilderness hot-zone!")
        val range = nextArea.wildernessLevelRange
        return Optional.of("The wilderness between levels ${range.first} and ${range.last} is now a hot zone.")
    }

    override fun delayUntilStart(): Duration =
        30.seconds

    override fun cancel() {
        messagePlayersInHotZone("The hot zone event has been cancelled.")
        activeArea = null
    }

    override fun completed(): Boolean {
        val area = activeArea ?: return true
        if (endTimer.elapsed()) {
            activeArea = null
            rewardHandler.giveRewards(area)
            return true
        } else
            return false
    }

    override fun stateDialogue(state: WildernessEvent.State): String {
        return when (state) {
            is WildernessEvent.State.Scheduled -> "The wilderness hot zone event starts in ${state.timeLeft.formattedString}."
            is WildernessEvent.State.Active -> "The wilderness between levels ${activeArea?.wildernessLevelRange?.let { "${it.first} and ${it.last}" }} is a hot zone for ${endTimer.durationRemaining().formattedString}."
        }
    }

    override fun source(): WildernessEventSource =
        WildernessEventSource.Standalone
}
