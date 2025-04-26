package com.near_reality.scripts.interfaces

import com.near_reality.game.coroutine.GameCoroutineTask
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.packet.PacketDispatcher
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import kotlinx.coroutines.withContext

/**
 * @author Jire
 */
class NamedInterfaceHandler(
    override val script: InterfaceScript,
    val name: String,
    val componentID: Int,
    val slotID: Int = Interface.DEFAULT_SLOT_ID,
    val handler: (suspend InterfaceHandlerContext.() -> Unit) = {}
) : InterfaceHandler {

    fun PacketDispatcher.sendComponentSettings(
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(script.`interface`, script.getComponent(name), start, end, *masks)

    fun sendComponentSettings(
        player: Player,
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = player.packetDispatcher.sendComponentSettings(start, end, *masks)

    fun PacketDispatcher.sendComponentSettings(
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(0, end, *masks)

    fun sendComponentSettings(
        player: Player,
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(player, 0, end, *masks)

    fun InterfaceHandlerContext.sendComponentSettings(
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = player.packetDispatcher.sendComponentSettings(start, end, *masks)

    fun InterfaceHandlerContext.sendComponentSettings(
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(0, end, *masks)

    fun PacketDispatcher.sendComponentText(text: Any) =
        sendComponentText(script.`interface`, script.getComponent(name), text)

    fun sendComponentText(player: Player, text: Any) = player.packetDispatcher.sendComponentText(text)

    fun InterfaceHandlerContext.sendComponentText(text: Any) = player.packetDispatcher.sendComponentText(text)

    override fun InterfaceHandlerContext.handle() {
        val task = GameCoroutineTask()
        val block = suspend {
            withContext(task) {
                handler()
            }
        }
        task.launch(block)
    }

}

class DragInterfaceHandler(
    val from: NamedInterfaceHandler,
    val to: NamedInterfaceHandler,
    val handler: Player.(fromSlot: Int, toSlot: Int) -> Unit
)