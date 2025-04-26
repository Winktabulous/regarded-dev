@file:Suppress("DuplicatedCode")

package com.near_reality.plugins.interfaces.death

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.format
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.start
import mgi.types.config.items.ItemDefinitions
import kotlin.math.min

/**
 * @author Kris | 14/06/2022
 */
class DeathsOfficeRetrievalInterface : InterfaceScript() {
    init {
        GameInterface.DEATHS_OFFICE_RETRIEVAL {
            val itemsLayer = "Items layer"(3) {
                if (option == 10) {
                    player.sendMessage(ItemDefinitions.get(itemID).examine)
                } else {
                    player.selectItem(slotID)
                }
            }

            "Claim 1"(6) {
                player.claimItem(1)
            }
            "Claim 5"(7) {
                player.claimItem(5)
            }
            "Claim X"(8) {
                player.sendInputInt("How many would you like to withdraw?") { value ->
                    if (value > 0) player.claimItem(value)
                }
            }
            "Claim All Of Single Item"(9) {
                player.claimItem(Int.MAX_VALUE)
            }
            "Claim All"(10) {
                val total = player.totalFeeSum()
                if (total == 0L) {
                    player.claimAll()
                } else {
                    player.dialogueManager.start {
                        options(
                            "Death will change ${total.format()} coins.",
                            Dialogue.DialogueOption("Pay Death's fee.") {
                                player.claimAll()
                            },
                            Dialogue.DialogueOption("Cancel")
                        )
                    }
                }
            }

            opened {
                gravestone.container.isFullUpdate = true
                gravestone.container.refresh(this)
                selectItem(-1)
                sendInterface()
                itemsLayer.sendComponentSettings(this, 119, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10)
            }
        }
    }

    fun Player.claimAll() {
        var runs = 0
        while (!gravestone.container.isEmpty) {
            val itemInSlot = gravestone.container.get(0) ?: return
            val sellPrice = itemInSlot.sellPrice
            val multiplier = if (isIronman) 2 else 1
            val threshold = 100_000 * multiplier
            val fee = if (sellPrice < threshold) {
                0
            } else {
                (sellPrice * (0.05 / multiplier)).toInt()
            }
            if (!claimItem(Int.MAX_VALUE, 0, fee)) break
            if (++runs >= 1000) {
                logger.warn("Saved claimAll() from infinite looping!")
                return
            }
        }
    }

    fun Player.totalFeeSum(): Long = gravestone.container.items.values.sumOf { item ->
        val sellPrice = item.sellPrice
        val multiplier = if (isIronman) 2 else 1
        val threshold = 100_000 * multiplier
        val fee = if (sellPrice < threshold) {
            0
        } else {
            (sellPrice * (0.05 / multiplier)).toInt()
        }
        fee.toLong() * item.amount
    }

    fun Player.selectItem(slot: Int) {
        varManager.sendVarInstant(262, slot)
        val itemInSlot = gravestone.container.get(slot)
        varManager.sendVarInstant(261, gravestone.coinsInCoffer.coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        val sellPrice = itemInSlot?.sellPrice ?: 0
        val multiplier = if (isIronman) 2 else 1
        val threshold = 100_000 * multiplier
        if (sellPrice < threshold) {
            varManager.sendVarInstant(263, 0)
        } else {
            val fee = (sellPrice * (0.05 / multiplier)).toInt()
            varManager.sendVarInstant(263, fee)
        }
    }

    fun Player.getCurrentSelectedSlot(): Int {
        return varManager.getValue(262)
    }

    fun Player.getFee(): Int {
        return varManager.getValue(263)
    }

    fun Player.claimItem(amount: Int, slot: Int = getCurrentSelectedSlot(), fee: Int = getFee()): Boolean {
        val item = gravestone.container.get(slot) ?: return false
        var totalAmount = amount.coerceAtMost(item.amount)
        if (item.isStackable) {
            val inInventory = inventory.getAmountOf(item.id)
            if (inInventory > 0) {
                totalAmount = min(totalAmount, Int.MAX_VALUE - inInventory)
            }
        } else {
            totalAmount = min(totalAmount, inventory.freeSlots)
        }
        if (totalAmount <= 0) {
            sendMessage("Not enough space in your inventory.")
            return false
        }
        val totalCost = fee * totalAmount.toLong()
        val inGravestone = gravestone.coinsInCoffer
        val inInventory = inventory.getAmountOf(ItemId.COINS_995)
        val inBank = bank.getAmountOf(ItemId.COINS_995)
        val totalCarried = inGravestone + inInventory + inBank
        if (totalCarried < totalCost) {
            sendMessage("You do not have enough funds to pay for the fee.")
            return false
        }
        var amountToRemove = totalCost
        if (amountToRemove > 0 && inGravestone > 0) {
            val toRemove = min(amountToRemove, inGravestone)
            amountToRemove -= toRemove
            gravestone.coinsInCoffer -= toRemove
            sendMessage("Payment has been taken from your coffer: ${toRemove.format()} coins")
        }
        if (amountToRemove > 0 && inBank > 0) {
            val toRemove = min(amountToRemove, inBank.toLong()).toInt()
            amountToRemove -= toRemove
            bank.remove(Item(ItemId.COINS_995, toRemove))
            bank.refreshContainer()
            sendMessage("Payment has been taken from your bank: ${toRemove.format()} coins")
        }
        if (amountToRemove > 0 && inInventory > 0) {
            val toRemove = min(amountToRemove, inInventory.toLong()).toInt()
            amountToRemove -= toRemove
            inventory.deleteItem(Item(ItemId.COINS_995, toRemove))
            inventory.refresh()
            sendMessage("Payment has been taken from your inventory: ${toRemove.format()} coins")
        }
        inventory.container.deposit(this, gravestone.container, slot, totalAmount)
        inventory.container.refresh(this)
        gravestone.container.refresh(this)
        varManager.sendVarInstant(261, gravestone.coinsInCoffer.coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        if (totalAmount >= item.amount) {
            gravestone.container.shift()
            gravestone.container.refresh(this)
            selectItem(-1)
        }
        return true
    }
}
