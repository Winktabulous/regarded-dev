package com.near_reality.game.content.tournament.preset

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import java.util.*

data class TournamentPresetSave(
    val inventory: Container,
    val equipment: Container,
    val runePouch: Container
) {

    fun apply(player: Player) {
        player.inventoryTemp.container.setContainer(inventory)
        player.equipmentTemp.container.setContainer(equipment)
        player.secondaryRunePouchTemp.container.setContainer(runePouch)
        player.inventoryTemp.refreshAll()
        player.equipmentTemp.refreshAll()
        player.secondaryRunePouchTemp.container.refresh(player)
    }

    companion object {

        fun ofPlayer(player: Player) = TournamentPresetSave(
            inventory = player.inventoryTemp.container.copy(),
            equipment = player.equipmentTemp.container.copy(),
            runePouch = player.secondaryRunePouchTemp.container.copy()
        )
        private fun Container.copy() = Container(policy, type, Optional.empty()).apply {
            setContainer(this@copy)
        }
    }
}
