package com.near_reality.game.model.item.degrading

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.DegradeType
import java.util.function.Function

interface Degradeable {

    val type: DegradeType
    val itemId: Int
    val nextId: Int
    val maximumCharges: Int
    val minimumCharges: Int

    val function: Function<Item, Array<Item>>?
}
