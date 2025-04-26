package com.zenyte.plugins.events

import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.Event

/**
 * @author Jire
 * @author Kris
 */
data class PostWindowStatusEvent(val player: Player) : Event