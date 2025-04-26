package com.near_reality.plugins.interfaces.death

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.content.gravestone.GravestoneExt.getItemValue
import com.zenyte.game.content.gravestone.GravestoneExt.getRetrievalServiceCache
import com.zenyte.game.content.gravestone.GravestoneExt.removeGravestone
import com.zenyte.game.content.gravestone.GravestoneExt.unlockGravestone
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue

/**
 * @author Kris | 12/06/2022
 */
class GravestoneReclaimInterface : InterfaceScript() {
    init {
        GameInterface.GRAVESTONE_RECLAIM {
            "Close"(3) {
                player.interfaceHandler.closeInterface(GameInterface.GRAVESTONE_RECLAIM)
            }
            val freeItemsLayer = "Interact with free items"(6) {
                player.reclaimItem(slotID)
            }
            val paidItemsLayer = "Interact with paid items".suspend(13) {
                if (player.retrievalService.isLocked) {
                    return@suspend player.sendMessage("You must first pay to unlock the items. Click the <col=ff0000>padlock</col> button.")
                }
                player.reclaimItem(slotID)
            }
            val incinerator = "Incinerator"(17)
            "Reclaim free items"(8) {
                player.reclaimAll(true)
            }
            "Reclaim paid items".suspend(15) {
                if (!player.retrievalService.isLocked) return@suspend player.reclaimAll(false)
                if (player.unlockGravestone()) player.refreshInterface()
            }
            drag(paidItemsLayer, incinerator) {
                if (!player.retrievalService.isLocked) {
                    return@drag player.sendMessage("You no longer need to discard items now that the fee has been paid.")
                }
                player.incinerate(fromSlotID)
            }
            opened {
                resortInterface()
                refreshInterface()
                sendInterface()
                freeItemsLayer.sendComponentSettings(this, 0, 119, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10)
                paidItemsLayer.sendComponentSettings(this, 0, 119, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10, AccessMask.DRAG_DEPTH2)
            }
        }
    }

    fun Player.reclaimItem(slotId: Int) {
        val item = retrievalService.container[slotId] ?: return
        inventory.container.deposit(this, retrievalService.container, slotId, item.amount)
        retrievalService.container.shift()
        inventory.refresh()
        retrievalService.container.refresh(this)
        if (retrievalService.container.isEmpty) removeGravestone() else refreshInterface()
    }

    fun Player.incinerate(slotId: Int) {
        val item = retrievalService.container[slotId] ?: return
        dialogueManager.start(object : Dialogue(this) {
            override fun buildDialogue() {
                options(
                    item.name,
                    DialogueOption("Destroy it") {
                        retrievalService.container.set(slotId, null)
                        retrievalService.container.shift()
                        retrievalService.container.refresh(this@incinerate)
                        if (retrievalService.container.isEmpty) removeGravestone() else refreshInterface()
                    },
                    DialogueOption("Keep it")
                )
            }
        })
    }

    fun Player.reclaimAll(free: Boolean) {
        val cache = getRetrievalServiceCache()
        val paidItemsCount = cache.highCostItems.size + cache.mediumCostItems.size + cache.lowCostItems.size
        if (free) {
            if (cache.freeItems.isEmpty()) return
            for (slotId in paidItemsCount until (paidItemsCount + cache.freeItems.size)) {
                val item = retrievalService.container[slotId] ?: continue
                inventory.container.deposit(this, retrievalService.container, slotId, item.amount)
            }
        } else {
            for (slotId in 0 until paidItemsCount) {
                val item = retrievalService.container[slotId] ?: continue
                inventory.container.deposit(this, retrievalService.container, slotId, item.amount)
            }
        }
        retrievalService.container.shift()
        inventory.refresh()
        retrievalService.container.refresh(this)
        if (retrievalService.container.isEmpty) removeGravestone() else refreshInterface()
    }

    fun Player.resortInterface() {
        val gravestoneContainer = retrievalService.container
        val allItems = gravestoneContainer.items.values.sortedByDescending {
            getItemValue(it)
        }
        gravestoneContainer.clear()
        val (freeItems, highCost, mediumCost, lowCost) = getRetrievalServiceCache(allItems)
        gravestoneContainer.addAll(highCost)
        gravestoneContainer.addAll(mediumCost)
        gravestoneContainer.addAll(lowCost)
        gravestoneContainer.addAll(freeItems)
        gravestoneContainer.isFullUpdate = true
        gravestoneContainer.refresh(this)
    }

    fun Player.refreshInterface() {
        val (freeItems, highCost, mediumCost, lowCost, sum)
                = getRetrievalServiceCache()
        val totalCount = highCost.size + mediumCost.size + lowCost.size
        varManager.sendBitInstant(10472, if (freeItems.isEmpty()) 0 else (totalCount + 1))
        varManager.sendBitInstant(10473, mediumCost.size + highCost.size + 1)
        varManager.sendBitInstant(10474, highCost.size + 1)
        packetDispatcher.sendClientScript(3478, if (!retrievalService.isLocked) 0 else sum, 0)
    }
}
