package com.near_reality.plugins.item

import com.near_reality.game.content.imbue.DisimbueItemHandler
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.model.item.enums.ImbueableItem

class DisimbueItemAction : ItemActionScript() {
    init {
        items(ImbueableItem.IMBUEABLES
            .filterNot {
                it.value.name.contains("SLAYER_HELM") ||
                        it.value.name.contains("BLACK_MASK") ||
                        it.value.name.startsWith("CRYSTAL_") ||
                        it.value == ImbueableItem.RING_OF_SUFFERING ||
                        it.value == ImbueableItem.RING_OF_SUFFERING_RI
            }
            .keys)

        "Uncharge" {
            DisimbueItemHandler.disimbueItem(player, item)
        }

    }
}
