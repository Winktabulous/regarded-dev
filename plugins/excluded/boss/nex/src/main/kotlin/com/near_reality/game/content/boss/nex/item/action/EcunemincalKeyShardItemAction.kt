package com.near_reality.game.content.boss.nex.item.action

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class EcunemincalKeyShardItemAction  : ItemActionScript() {
    init {
        items(ItemId.ECUMENICAL_KEY_SHARD)

        "Combine" {
            if (item.amount < 50) {
                player.dialogue { plain("You need at least 50 ${item.name} to create a key.") }
            } else {
                player.options("Would you like to combine 50 ${item.name} to create a key?") {
                    dialogueOption("Yes.", noPlayerMessage = true, action = {
                        if(player.inventory.hasFreeSlots()) {
                            if (player.inventory.deleteItem(item.id, 50).result == RequestResult.SUCCESS) {
                                val key = Item(ItemId.ECUMENICAL_KEY, 1)
                                player.inventory.addItem(key)
                                player.dialogue {
                                    doubleItem(item, key, "You combine 50 shards to create a key.")
                                }
                            }
                        } else {
                            player.sendMessage("Please free up at least one inventory space to do this.")
                        }

                    })
                    dialogueOption("No.") {}
                }
            }
        }

    }
}

