package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId

class SledItemAction : ItemActionScript() {
    init {
        items(ItemId.SLED, ItemId.SLED_4084, ItemId.SLED_25282)

        "Ride" {
            player.equipment.wear(slotID)
        }
    }
}
