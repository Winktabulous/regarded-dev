package com.near_reality.game.content.shop

import com.near_reality.game.content.universalshop.UnivShopTable

data class UniversalShopCategory(
    val uniqueIndex: Int,
    val tableName: String,
    val tableId: Int,
    val listSlot: Int,
    var componentIds: IntRange = 0..1,
    var itemCount : Int = 0,
    val table: UnivShopTable
) {
    init {
        table.run {
            val size = items.size
            val start = (4 + ((uniqueIndex - 1) * 4) + offset)
            val end = start + size
            componentIds = start..end
            offset += size
        }
    }

    companion object {
        @JvmStatic var offset = 0
    }
}