package com.near_reality.game.content.araxxor.items

import com.near_reality.game.util.Ticker
import com.zenyte.game.item.ItemId.ARANEA_BOOTS
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-02
 */
class AraneaBoots(
    val player: Player
) {

    private val specialAttackTimer: Ticker = Ticker(8)

    fun isPlayerWearingBoots(): Boolean = player.equipment?.getItem(EquipmentSlot.BOOTS)?.id == ARANEA_BOOTS
    fun processBootTick() = specialAttackTimer.tick()


    private fun hasActivated(): Boolean {
        if (!specialAttackTimer.finished) return false
        if (!isPlayerWearingBoots()) return false
        player.sendMessage("Your Aranea boots let you avoid the spider-based attack.")
        specialAttackTimer.reset()
        return true
    }

    fun isPlayerWebImmune(): Boolean {
        return if (isPlayerWearingBoots()) hasActivated()
        else false
    }

}