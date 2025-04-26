package com.zenyte.game.content.minigame.duelarena

import com.zenyte.game.world.entity.player.container.Container

fun Duel.updateInventories() {
    player.packetDispatcher.sendClientScript(10693, *opponent.inventory.container.toArray())
    player.packetDispatcher.sendClientScript(10694, *opponent.equipment.container.toArray())
    opponent.packetDispatcher.sendClientScript(10693, *player.inventory.container.toArray())
    opponent.packetDispatcher.sendClientScript(10694, *player.equipment.container.toArray())
}

private fun Container.toArray() = Array(containerSize) { get(it)?.id?:-1 }