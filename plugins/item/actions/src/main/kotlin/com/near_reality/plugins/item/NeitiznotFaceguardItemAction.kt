package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId

class NeitiznotFaceguardItemAction : ItemActionScript() {
    init {
        items(ItemId.NEITIZNOT_FACEGUARD)

        "Dismantle" {
            player.dialogueManager.start(NeitiznotFaceguardDismantleDialogue(player))
        }
    }
}
