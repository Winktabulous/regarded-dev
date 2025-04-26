package com.near_reality.scripts.`object`.actions

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
data class ObjectHandlerContext(
    val player: Player,
    val obj: WorldObject,
    val name: String?,
    val optionID: Int,
    val option: String?
)