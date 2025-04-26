package com.near_reality.game.content.wilderness.revenant.npc.drop

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.player.Player

private const val BRACELET_RATE: Int = 100
private const val EMBLEM_RATE: Int = 500
private const val TOTEM_RATE: Int = 600
private const val STATUE_RATE: Int = 700
private const val MEDALLION_RATE: Int = 800
private const val EFFIGY_RATE: Int = 900
private const val RELIC_RATE: Int = 1000
private const val CRYSTAL_RATE: Int = 250
private const val AMULET_RATE: Int = 1200
private const val VIGGORAS_RATE: Int = 3000
private const val CRAWS_RATE: Int = 3000
private const val THAMMARONS_RATE: Int = 3000

@Suppress("unused")
class RevenantImpProcessor : DropProcessor() {

    override fun attach() {
        appendDrop(DisplayedDrop(ItemId.BRACELET_OF_ETHEREUM, 1, 1, BRACELET_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_EMBLEM, 1, 1, EMBLEM_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_TOTEM, 1, 1, TOTEM_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_STATUETTE, 1, 1, STATUE_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_MEDALLION, 1, 1, MEDALLION_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_EFFIGY, 1, 1, EFFIGY_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_RELIC, 1, 1, RELIC_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.ANCIENT_CRYSTAL, 1, 1, CRYSTAL_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.AMULET_OF_AVARICE, 1, 1, AMULET_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.VIGGORAS_CHAINMACE_U, 1, 1, VIGGORAS_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.CRAWS_BOW_U, 1, 1, CRAWS_RATE.toDouble()))
        appendDrop(DisplayedDrop(ItemId.THAMMARONS_SCEPTRE_U, 1, 1, THAMMARONS_RATE.toDouble()))
    }

    override fun ids(): IntArray {
        return intArrayOf(NpcId.REVENANT_IMP)
    }

    override fun drop(npc: NPC, killer: Player, drop: Drop, item: Item): Item {
        if (randomDrop(killer, getRate(THAMMARONS_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.THAMMARONS_SCEPTRE_U)
        }
        if (randomDrop(killer, getRate(CRAWS_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.CRAWS_BOW_U)
        }
        if (randomDrop(killer, getRate(VIGGORAS_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.VIGGORAS_CHAINMACE_U)
        }
        if (randomDrop(killer, getRate(AMULET_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.AMULET_OF_AVARICE)
        }
        if (randomDrop(killer, getRate(CRYSTAL_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_CRYSTAL)
        }
        if (randomDrop(killer, getRate(RELIC_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_RELIC)
        }
        if (randomDrop(killer, getRate(EFFIGY_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_EFFIGY)
        }
        if (randomDrop(killer, getRate(MEDALLION_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_MEDALLION)
        }
        if (randomDrop(killer, getRate(STATUE_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_STATUETTE)
        }
        if (randomDrop(killer, getRate(TOTEM_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_TOTEM)
        }
        if (randomDrop(killer, getRate(EMBLEM_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.ANCIENT_EMBLEM)
        }
        if (randomDrop(killer, getRate(BRACELET_RATE, killer.variables.isSkulled)) == 0) {
            return Item(ItemId.BRACELET_OF_ETHEREUM)
        }
        return item
    }

    private fun getRate(rate: Int, skulled: Boolean): Int {
        return if (skulled) (rate.toDouble() / 1.10).toInt()
        else rate
    }
}
