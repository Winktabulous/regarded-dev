package com.near_reality.game.content.dt2.npc.theduke

import com.zenyte.game.util.Utils.getDistance
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class GasVent(var cooldown: Int = 3): NPC(12198) {

    override fun postInit() {
        this.radius = 0
    }

    operator fun contains(player: Player): Boolean {
        return player.location.getAxisDistance(1, this.location, this.size) <= 1
    }
}