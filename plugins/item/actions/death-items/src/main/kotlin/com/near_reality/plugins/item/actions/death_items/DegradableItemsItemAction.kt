package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.ACCURSED_SCEPTRE_27665
import com.zenyte.game.item.ItemId.AMULET_OF_BLOOD_FURY
import com.zenyte.game.item.ItemId.BONECRUSHER
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C
import com.zenyte.game.item.ItemId.BRACELET_OF_ETHEREUM
import com.zenyte.game.item.ItemId.CRAWS_BOW
import com.zenyte.game.item.ItemId.RING_OF_SUFFERING_I
import com.zenyte.game.item.ItemId.RING_OF_SUFFERING_RI
import com.zenyte.game.item.ItemId.THAMMARONS_SCEPTRE
import com.zenyte.game.item.ItemId.URSINE_CHAINMACE_27660
import com.zenyte.game.item.ItemId.VIGGORAS_CHAINMACE
import com.zenyte.game.item.ItemId.WEBWEAVER_BOW_27655
import com.zenyte.game.model.item.degradableitems.DegradableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class DegradableItemsItemAction : ItemActionScript() {

    private val degradableItems = DegradableItem.ITEMS.keys
        .filterNot {
            it == BONECRUSHER
                    || it == CRAWS_BOW
                    || it == WEBWEAVER_BOW_27655
                    || it == THAMMARONS_SCEPTRE
                    || it == ACCURSED_SCEPTRE_27665
                    || it == VIGGORAS_CHAINMACE
                    || it == URSINE_CHAINMACE_27660
                    || it == RING_OF_SUFFERING_RI
                    || it == RING_OF_SUFFERING_I
                    || it == BRACELET_OF_ETHEREUM
                    || it == AMULET_OF_BLOOD_FURY
                    || it == BOW_OF_FAERDHINEN
                    || it == BOW_OF_FAERDHINEN_C
        }

    init {
        items(degradableItems)

        death {
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
            lost {
                if (!pvp) {
                    return@lost yield(item)
                }
                val degradable = DegradableItem.ITEMS[item.id]
                requireNotNull(degradable)
                yield(Item(DegradableItem.getCompletelyDegradedId(item.id, true)))
                val function = degradable.function
                if (function != null) {
                    val result = function.apply(item)
                    for (other in result) {
                        if (other.amount <= 0) continue
                        yield(other)
                    }
                }
            }
        }
    }
}
