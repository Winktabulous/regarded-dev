package com.near_reality.game.content.bountyhunter.tasks

import com.near_reality.game.content.bountyhunter.BHInterfaceUpdate
import com.near_reality.game.content.bountyhunter.BountyHunterController
import com.zenyte.game.task.WorldTask
import com.zenyte.game.world.entity.player.Player

/**
 * This task runs at given intervals to update all minigame
 * participants with the latest data regarding their target
 * @author John J. Woloszyk / Kryeus
 */
class BountyHunterUpdatePlayerInterfaces : WorldTask {

    override fun run() {
        val pendingRemovals = mutableListOf<Player>()
        val iterator = BountyHunterController.eligiblePlayersInWilderness.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            if(player.isNulled || player.isFinished) {
                pendingRemovals.add(player)
                continue
            }
            BountyHunterController.queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_TARGET_ALL, player)
        }
        for(player in pendingRemovals) {
            BountyHunterController.eligiblePlayersInWilderness.remove(player)
        }
    }
}