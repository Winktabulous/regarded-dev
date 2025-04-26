package com.zenyte.game.world.entity.npc.actions

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
data class NPCOptionHandler(
    val player: Player,
    val npc: NPC
)