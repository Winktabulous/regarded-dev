package com.near_reality.game.content.tournament.preset.component

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.DegradableItem
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Tommeh | 25/05/2019 | 16:00
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
@JvmRecord
data class EquipmentComponent(val items: Map<Int, BooleanEntry<Item>?>) {
    class Builder {
        private val items: MutableMap<Int, BooleanEntry<Item>?> = HashMap()

        init {
            for (slot in 0 until Equipment.SIZE) {
                items[slot] = null
            }
        }

        fun put(slot: EquipmentSlot, id: Int, droppable: Boolean): Builder {
            return put(slot, id, 1, droppable)
        }

        fun put(slot: EquipmentSlot, id: Int, amount: Int, droppable: Boolean): Builder {
            items[slot.slot] = BooleanEntry(
                Item(
                    id,
                    amount,
                    DegradableItem.getFullCharges(id)
                ), droppable
            )
            return this
        }

        fun build(): EquipmentComponent {
            return EquipmentComponent(items)
        }
    }
}
