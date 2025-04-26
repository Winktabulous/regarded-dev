package com.near_reality.scripts.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.SwitchPlugin
import com.zenyte.game.packet.PacketDispatcher
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 * @author Kris
 */
@KotlinScript(
    "Interface Script",
    "interface.kts",
    compilationConfiguration = InterfaceCompilation::class
)
@Suppress(
    "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE",
    "UNUSED_VALUE",
    "KotlinRedundantDiagnosticSuppress"
)
abstract class InterfaceScript : Interface(), InterfacesScript, SwitchPlugin {

    private lateinit var gameInterface: GameInterface

    operator fun GameInterface.invoke(block: InterfaceScript.() -> Unit) {
        gameInterface = this

        block()
    }

    private var opened: (suspend Player.() -> Unit)? = null

    fun opened(block: suspend Player.() -> Unit) {
        opened = block
    }

    override fun open(player: Player) {
        val opened = opened
        if (opened == null) super.open(player)
        else runBlocking {
            player.opened()
        }
    }

    private var closed: (suspend Player.() -> Unit)? = null

    fun closed(block: suspend Player.() -> Unit) {
        closed = block
    }

    override fun close(player: Player, replacement: Optional<GameInterface?>?) {
        val closed = closed
        if (closed == null) super.close(player, null)
        else runBlocking {
            player.closed()
        }
    }

    private val scriptHandlers: ObjectList<NamedInterfaceHandler> = ObjectArrayList()

    private val dragScriptHandlers: ObjectList<DragInterfaceHandler> = ObjectArrayList()

    override fun attach() {
        for (i in 0..scriptHandlers.lastIndex) {
            val handler = scriptHandlers[i]
            put(handler.componentID, handler.slotID, handler.name)
        }
    }

    override fun build() {
        for (i in 0..scriptHandlers.lastIndex) {
            val handler = scriptHandlers[i]
            bind(handler.name, handler)
        }
        for (handler in dragScriptHandlers) {
            bind(handler.from.name, handler.to.name) { player, fromSlot, toSlot ->
                handler.handler.invoke(player, fromSlot, toSlot)
            }
        }
    }

    fun String.suspend(
        componentID: Int,
        slotID: Int = DEFAULT_SLOT_ID,
        handler: (suspend InterfaceHandlerContext.() -> Unit)
    ) = NamedInterfaceHandler(this@InterfaceScript, this, componentID, slotID, handler).apply {
        scriptHandlers.add(this)
    }

    fun drag(
        from: NamedInterfaceHandler,
        to: NamedInterfaceHandler,
        handler: (InterfaceDragHandlerContext.() -> Unit)
    ) {
        dragScriptHandlers.add(DragInterfaceHandler(from, to) { fromSlot, toSlot ->
            handler.invoke(
                InterfaceDragHandlerContext(this@InterfaceScript, this@DragInterfaceHandler, fromSlot, toSlot)
            )
        })
    }

    operator fun String.invoke(
        componentID: Int,
        slotID: Int = DEFAULT_SLOT_ID,
        handler: (InterfaceHandlerContext.() -> Unit) = {}
    ) = suspend(componentID, slotID) {
        handler(this)
    }

    override fun getInterface() = gameInterface

    var interruptedOnLock = DEFAULT_IS_INTERRUPTED_ON_LOCK

    override fun isInterruptedOnLock() = interruptedOnLock

    fun Player.sendInterface() = interfaceHandler.sendInterface(this@InterfaceScript)

    fun Player.textComponent(componentID: Int, defaultValue: String = "") =
        TextComponent(this@InterfaceScript, componentID, this, defaultValue)

    fun PacketDispatcher.sendComponentSettings(
        componentID: Int,
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(`interface`, componentID, start, end, *masks)

    fun Player.sendComponentSettings(
        componentID: Int,
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = packetDispatcher.sendComponentSettings(componentID, start, end, *masks)

    fun PacketDispatcher.sendComponentSettings(
        componentID: Int,
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(componentID, 0, end, *masks)

    fun Player.sendComponentSettings(
        componentID: Int,
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(componentID, 0, end, *masks)

    fun PacketDispatcher.sendComponentSettings(
        componentName: String,
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(getComponent(componentName), start, end, *masks)

    fun Player.sendComponentSettings(
        componentName: String,
        start: Int = 0, end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(getComponent(componentName), start, end, *masks)

    fun PacketDispatcher.sendComponentSettings(
        componentName: String,
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(componentName, 0, end, *masks)

    fun Player.sendComponentSettings(
        componentName: String,
        end: Int,
        vararg masks: AccessMask
    ) = sendComponentSettings(componentName, 0, end, *masks)

}
