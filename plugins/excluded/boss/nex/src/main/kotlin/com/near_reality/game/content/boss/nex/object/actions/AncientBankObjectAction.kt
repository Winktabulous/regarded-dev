package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.GameInterface
import com.zenyte.game.content.ItemRetrievalService
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId

class AncientBankObjectAction : ObjectActionScript() {
    init {
        ObjectId.CHEST_42854 {
            when(option) {
                "Claim" -> {
                    val service = player.retrievalService
                    if (service.type != ItemRetrievalService.RetrievalServiceType.ANCIENT_PRISON || service.container.size == 0) {
                        player.dialogue {
                            plain("The chest seems to be empty. If it did have any of your items, but<br><br>you died before collecting them, they'll now be lost.")
                        }
                    } else
                        GameInterface.ITEM_RETRIEVAL_SERVICE.open(player)
                }
            }
        }

    }
}

