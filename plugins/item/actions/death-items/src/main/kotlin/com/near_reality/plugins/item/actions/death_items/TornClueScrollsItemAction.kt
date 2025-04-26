package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class TornClueScrollsItemAction : ItemActionPlugin() {
    init {
        items(TORN_CLUE_SCROLL_PART_1, TORN_CLUE_SCROLL_PART_2, TORN_CLUE_SCROLL_PART_3)

        death {
            kept {
                yield(item)
            }
            status {
                ItemDeathStatus.KEEP_ON_DEATH
            }
        }
    }
}
