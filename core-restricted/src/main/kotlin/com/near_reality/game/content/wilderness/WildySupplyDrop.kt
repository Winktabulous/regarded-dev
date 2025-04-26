//package com.near_reality.game.content.wilderness
//
//import com.near_reality.game.item.CustomItemId
//import com.zenyte.game.item.Item
//import com.zenyte.game.model.item.pluginextensions.ItemPlugin
//import com.zenyte.game.util.Utils
//import com.zenyte.game.world.entity.player.Player
//
//class WildySupplyDrop {
//    sealed interface Table {
//        fun loots() : Collection<TableItem>
//        fun item() : Item = loots().shuffled().take(1).last().create()
//    }
//
//    data class TableItem(val itemId : Int, val min : Int = 1, val max : Int = 1, val noted : Boolean = false) {
//        private fun quantity() = Utils.random(min, max)
//        fun create(): Item = if(noted) Item(itemId, quantity()).toNote() else Item(itemId, quantity())
//    }
//
//    data object SupplyTable: Table {
//        override fun loots(): Collection<TableItem> =
//            listOf(
//                TableItem(3025)
//
//            )
//    }
//
//
//
//    @Suppress("unused")
//    object Action : ItemPlugin() {
//        override fun handle() {
//            bind("Open") { p: Player, item: Item, _: Int ->
//
//            }
//        }
//
//        override fun getItems(): IntArray = intArrayOf(CustomItemId.WILDY_SUPPLY_SACK, CustomItemId.WILDY_SUPPLY_CRATE, CustomItemId.WILDY_SUPPLY_HOARD)
//    }
//}