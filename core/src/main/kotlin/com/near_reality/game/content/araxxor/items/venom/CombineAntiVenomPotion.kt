package com.near_reality.game.content.araxxor.items.venom

import com.zenyte.game.content.boons.impl.Mixologist
import com.zenyte.game.content.consumables.drinks.Potion
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.container.RequestResult
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-01
 */
class CombineAntiVenomPotion(
    val amount: Int,
    private val potToUpgrade: Int
) : Action() {

    var completed: Int = 0
    private val upgradedPot: Int = pots[potToUpgrade]

    override fun processWithDelay(): Int {
        val potion = player.inventory.getAny(potToUpgrade)
        val dose = Potion.EXTENDED_ANTI_VENOM.getDoses(upgradedPot)
        if (player.inventory.deleteItems(Item(ARAXYTE_VENOM_SACK, dose), potion).result == RequestResult.SUCCESS) {
            var amt = 1
            if (player.boonManager.hasBoon(Mixologist::class.java) && Mixologist.roll()) {
                player.sendFilteredMessage("Your Mixology boon grants you an additional potion.")
                amt += 1
                player.skills.addXp(SkillConstants.HERBLORE, 25.5 * dose)
            }
            player.inventory.addOrDrop(upgradedPot, amt)
            player.skills.addXp(SkillConstants.HERBLORE, 25.5 * dose)
            completed++
            return 1
        }
        return 0
    }

    override fun start(): Boolean = check()

    override fun process(): Boolean = check()

    private fun check(): Boolean {
        val dose = Potion.EXTENDED_ANTI_VENOM.getDoses(upgradedPot)
        if (completed >= amount) return false
        return player.carryingItem(potToUpgrade) && player.inventory.getAmountOf(ARAXYTE_VENOM_SACK) >= dose
    }

    companion object {
        val pots: Int2IntOpenHashMap = Int2IntOpenHashMap(
            // anti-venom
            intArrayOf(ANTIVENOM1_12919, ANTIVENOM2_12917, ANTIVENOM3_12915, ANTIVENOM4_12913),
            // extended anti-venom
            intArrayOf(EXTENDED_ANTIVENOM1, EXTENDED_ANTIVENOM2, EXTENDED_ANTIVENOM3, EXTENDED_ANTIVENOM4)
        )
    }
}