package com.near_reality.game.content.gauntlet.item.actions

import com.near_reality.game.content.gauntlet.gauntlet
import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId

class EscapeCrystalItemAction : ItemActionScript() {
    init {
        items(ItemId.ESCAPE_CRYSTAL, ItemId.CORRUPTED_ESCAPE_CRYSTAL)

        "Activate" {
            player.gauntlet
                ?.end(GauntletRewardType.NONE, true, false)
                ?:run { player.sendDeveloperMessage("Gauntlet attribute is missing.") }
        }
    }
}
