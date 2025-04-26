package com.near_reality.game.content.boss.nex.item.equip

import com.near_reality.scripts.item.equip.EquipHandlerResponse
import com.near_reality.scripts.item.equip.ItemEquipScript
import com.zenyte.game.item.ItemId.*

class TorvaItemEquip : ItemEquipScript() {
    init {
        items(TORVA_FULLHELM_DAMAGED, TORVA_PLATEBODY_DAMAGED, TORVA_PLATELEGS_DAMAGED)

        intercept {
            val type = when(item.id) {
                TORVA_FULLHELM_DAMAGED -> "helm"
                TORVA_PLATEBODY_DAMAGED -> "platebody"
                TORVA_PLATELEGS_DAMAGED -> "platelegs"
                else -> error("Unknown torva type $item")
            }
            player.sendMessage("This $type is currently too malformed to equip. It needs repairing somehow.")
            EquipHandlerResponse.Negate
        }

    }
}
