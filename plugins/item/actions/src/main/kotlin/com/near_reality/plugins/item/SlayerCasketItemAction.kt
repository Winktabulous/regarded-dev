package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.dialogue.Dialogue

class SlayerCasketItemAction : ItemActionScript() {
    init {
        items(ItemId.CASKET_7956)

        "Open" {
            with(player.inventory) {
                ifDeleteItem(Item(item.id, 1)) {
                    val (item, descriptor) = if (Utils.randomBoolean(15))
                        Item(setOf(ItemId.SLAYER_TASK_RESET_SCROLL, ItemId.SLAYER_TASK_PICKER_SCROLL).random(), 1) to "a scroll"
                    else {
                        var coinAmount = (25_000..75_000).random()
                        if (World.hasBoost(XamphurBoost.DOUBLE_COINS))
                            coinAmount *= 2
                        Item(ItemId.COINS_995, coinAmount) to "some coins"
                    }
                    addOrDrop(item)
                    player.dialogueManager.start(object : Dialogue(player) {
                        override fun buildDialogue() {
                            doubleItem(item, item, "You open the casket and find $descriptor!")
                        }
                    })
                }
            }
        }
    }
}
