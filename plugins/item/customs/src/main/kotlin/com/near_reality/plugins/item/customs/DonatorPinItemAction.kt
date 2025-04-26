package com.near_reality.plugins.item.customs

import com.near_reality.game.content.middleman.middleManController
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId.DONATOR_PIN_10
import com.zenyte.game.item.ItemId.DONATOR_PIN_100
import com.zenyte.game.item.ItemId.DONATOR_PIN_25
import com.zenyte.game.item.ItemId.DONATOR_PIN_50

class DonatorPinItemAction : ItemActionScript() {
    init {
        items(DONATOR_PIN_10, DONATOR_PIN_25, DONATOR_PIN_50, DONATOR_PIN_100)

        "Redeem" {
            player.dialogueManager.start(DonatorPinRedeemDialogue(player, item))
        }
        "Trade" {
            player.middleManController.openTradeRequestInterface(item.id, 1, "")
        }

    }
}
