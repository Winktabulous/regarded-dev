package com.near_reality.game.content.bountyhunter.tasks

import com.near_reality.game.world.entity.player.bountyHunterInfoCooldown
import com.near_reality.game.world.entity.player.bountyHunterInterfaceRateLimit
import com.zenyte.game.task.WorldTask
import com.zenyte.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 */
class BountyHunterPlayerCooldown(val player: Player) : WorldTask {

    override fun run() {
        if (player.isNulled || player.isFinished) {
            super.stop()
            return
        }

        if (player.bountyHunterInfoCooldown == 0) {
            player.bountyHunterInterfaceRateLimit = 0
            super.stop()
            return
        }

        player.bountyHunterInfoCooldown--
    }

}