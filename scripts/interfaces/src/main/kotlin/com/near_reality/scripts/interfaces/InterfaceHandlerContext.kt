package com.near_reality.scripts.interfaces

import com.zenyte.game.world.entity.player.Player
import kotlinx.coroutines.cancel
import kotlin.coroutines.coroutineContext

/**
 * @author Jire
 */
class InterfaceHandlerContext(
    val script: InterfaceScript,
    val player: Player,
    val slotID: Int,
    val itemID: Int,
    val option: Int
) {

    suspend fun stop() = coroutineContext.cancel()

    suspend fun stop(condition: Boolean, whenStopped: suspend () -> Unit = {}) {
        if (condition) {
            whenStopped()
            stop()
        }
    }

    suspend fun stop(condition: Boolean, whenStoppedMessage: String) = stop(condition) {
        player.sendMessage(whenStoppedMessage)
    }

    suspend fun case(condition: Boolean, whenMet: suspend () -> Unit) {
        if (condition) whenMet()
    }

    suspend fun items(vararg itemIDs: Int, whenMet: suspend () -> Unit) = case(itemIDs.contains(itemID), whenMet)

    suspend fun item(itemID: Int, whenMet: suspend () -> Unit) = case(this.itemID == itemID, whenMet)

    suspend fun stopItem(itemID: Int, condition: Boolean = true, whenStoppedMessage: String) =
        stop(this.itemID == itemID && condition, whenStoppedMessage)

}

class InterfaceDragHandlerContext(
    val script: InterfaceScript,
    val player: Player,
    val fromSlotID: Int,
    val toSlotID: Int,
)