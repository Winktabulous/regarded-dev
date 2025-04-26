package com.near_reality.game.content

import com.zenyte.game.item._Item

data class CollectionLogRewardSet(
    val item0 : Int = -1,
    val quantity0: Int = -1,
    val item1 : Int = -1,
    val quantity1: Int = -1,
    val item2 : Int = -1,
    val quantity2: Int = -1,
    val item3 : Int = -1,
    val quantity3: Int = -1,
) {
    fun toItems() : List<_Item> {
        return mutableListOf(_Item(item0, quantity0), _Item(item1, quantity1), _Item(item2, quantity2), _Item(item3, quantity3))
    }
}