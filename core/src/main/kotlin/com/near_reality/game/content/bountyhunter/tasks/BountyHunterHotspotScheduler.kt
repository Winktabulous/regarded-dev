package com.near_reality.game.content.bountyhunter.tasks

import com.near_reality.game.content.bountyhunter.BountyHunterController
import com.near_reality.game.content.bountyhunter.BountyHunterHotspot
import com.near_reality.game.content.bountyhunter.BountyHunterVars
import com.zenyte.game.GameConstants.DEV_DEBUG
import com.zenyte.game.task.WorldTask
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.utils.TimeUnit
import java.time.Instant

/**
 * This represents the world task that schedules a new [BountyHunterHotspot]
 * at given intervals.
 * @author John J. Woloszyk / Kryeus
 */
class BountyHunterHotspotScheduler : WorldTask {

    override fun run() {
        val former = BountyHunterController.currentHotspot
        BountyHunterController.currentHotspot = BountyHunterHotspot.randomExcludeCurrent()
        BountyHunterController.currentHotspot.spawnBoundary()
        former.removeBoundary()
        val next = if (DEV_DEBUG)
            TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_DEV.toLong()).toInt()
        else TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_MAIN.toLong()).toInt()
        BountyHunterController.countdownTimer = Instant.now().plusSeconds(TimeUnit.TICKS.toSeconds(next.toLong()))
        WorldBroadcasts.broadcast(null, BroadcastType.BOUNTY_HUNTER, BountyHunterController.currentHotspot.zoneName)
    }
}