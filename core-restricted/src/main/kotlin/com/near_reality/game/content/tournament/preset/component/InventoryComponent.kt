package com.near_reality.game.content.tournament.preset.component

import com.zenyte.game.item.Item

/**
 * @author Tommeh | 25/05/2019 | 16:11
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
data class InventoryComponent(val items: List<BooleanEntry<Item>>) {
    class Builder {
        private val items: MutableList<BooleanEntry<Item>> = ArrayList()

        fun add(id: Int, droppable: Boolean): Builder {
            return add(id, 1, droppable)
        }

        fun add(id: Int, amount: Int, droppable: Boolean): Builder {
            items.add(BooleanEntry(Item(id, amount), droppable))
            return this
        }

        fun build(): InventoryComponent {
            return InventoryComponent(items)
        }
    }
}
