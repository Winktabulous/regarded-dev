package com.near_reality.scripts.interfaces.user

import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
data class UserInterfaceClickEvent(
    val player: Player,
    val componentID: Int,
    val slotID: Int,
    val itemID: Int,
    val optionID: Int,
    val option: String?
)