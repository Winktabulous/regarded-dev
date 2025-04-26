package com.zenyte.game.packet

import com.zenyte.game.world.entity.player.container.impl.ContainerType

fun PacketDispatcher.sendItemOptionScript(
    scriptId: Int,
    interfaceId: Int,
    containerComponentId: Int,
    containerType: ContainerType,
    vararg options: String,
    scrollBarComponentId: Int = -1,
) {
    sendClientScript(scriptId,
        interfaceId shl 16 or containerComponentId,
        containerType.id, 4, 7, 0,
        if (scrollBarComponentId == -1) -1 else interfaceId shl 16 or scrollBarComponentId,
        *options
    )
}