package com.near_reality.game.content.middleman

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.dialogue.PlainChat
import com.zenyte.plugins.dialogue.SkillDialogue

fun promptDPinSelectionAndOpenMiddlemanInterface(player: Player) {
    val donatorPinsInInventory: MutableList<Item> = ArrayList()
    for (itemId in MiddleManConstants.donatorPinItemIds) {
        val donatorPinItem = player.inventory.getAny(itemId)
        if (donatorPinItem != null) donatorPinsInInventory.add(donatorPinItem)
    }
    if (donatorPinsInInventory.isEmpty()) {
        player.dialogueManager.start(PlainChat(player, "You do not have any donator pins in your inventory."))
        return
    }
    player.dialogueManager.start(object : SkillDialogue(
        player, "Select the donator pin you'd like to trade", *donatorPinsInInventory.toTypedArray()
    ) {
        override fun run(slotId: Int, amount: Int) {
            val selectedDonatorPinItem = donatorPinsInInventory[slotId]
            player.middleManController
                .openTradeRequestInterface(selectedDonatorPinItem.id, amount, "")
        }
    })
}
