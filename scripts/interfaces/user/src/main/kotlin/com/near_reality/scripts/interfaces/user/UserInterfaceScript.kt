package com.near_reality.scripts.interfaces.user

import com.near_reality.scripts.interfaces.InterfacesScript
import com.zenyte.game.model.ui.UserInterface
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "User Interface Script",
    "userinterface.kts",
    compilationConfiguration = UserInterfaceCompilation::class,
)
abstract class UserInterfaceScript : UserInterface, InterfacesScript {

    private val idToHandler: Int2ObjectMap<UserInterfaceHandler> = Int2ObjectArrayMap()

    operator fun Int.invoke(build: UserInterfaceHandler.() -> Unit) {
        val handler = UserInterfaceHandler(this).apply(build)
        idToHandler.put(this, handler)
    }

    override fun getInterfaceIds(): IntArray = idToHandler.keys.toIntArray()

    override fun handleComponentClick(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        slotId: Int,
        itemId: Int,
        optionId: Int,
        option: String?
    ) {
        val handler = idToHandler.get(interfaceId) ?: return
        handler.handle(UserInterfaceClickEvent(player, componentId, slotId, itemId, optionId, option))
    }

}