package com.near_reality.content.group_ironman

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.*
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting
import mgi.types.config.items.ItemDefinitions
import java.util.*

class IronmanGroupStorageContainer : Container(ContainerPolicy.ALWAYS_STACK, ContainerType.GIM_BANK, Optional.empty()) {

    var storageSpaces = 200

    fun switchItem(fromSlot: Int, toSlot: Int, insert: Boolean) {
        var toSlot = toSlot
        if (toSlot >= containerSize) {
            toSlot = containerSize - 1
        }
        val fromItem = get(fromSlot)
        val toItem = get(toSlot)
        if (!insert) {
            set(fromSlot, toItem)
            set(toSlot, fromItem)
        } else {
            if (fromSlot < toSlot) {
                for (i in fromSlot + 1..toSlot) {
                    set(i - 1, get(i))
                }
                set(toSlot, fromItem)
            } else {
                for (i in fromSlot - 1 downTo toSlot) {
                    set(i + 1, get(i))
                }
                set(toSlot, fromItem)
            }
        }
    }

    override fun add(requestedItem: Item): ContainerResult {
        var item = if (requestedItem == null) null else Item(
            requestedItem.id,
            requestedItem.amount,
            requestedItem.attributesCopy
        )
        val result = ContainerResult(item, ContainerState.ADD)
        val definitions = item?.definitions
        if (definitions == null) {
            result.result = RequestResult.FAILURE
            return result
        }
        if (definitions.isNoted) {
            item!!.id = definitions.notedId
        }
        var id = item!!.id

        val requestedAmount = item!!.amount
        if (requestedAmount <= 0) {
            result.result = RequestResult.SUCCESS
            return result
        }
        val stackable = definitions.isStackable()
        val cantDeposit = containerSize - availableSlots.size >= storageSpaces
        if (availableSlots.isEmpty() || cantDeposit) {
            if (stackable && policy == ContainerPolicy.NORMAL || policy == ContainerPolicy.ALWAYS_STACK) {
                val slot = getSlotOf(id)
                if (slot != -1) {
                    val existingItem = get(slot)
                    if (!existingItem.hasAttributes()) {
                        val amount = existingItem.amount
                        if (amount + item!!.amount < 0) {
                            set(slot, Item(id, Int.MAX_VALUE, item!!.attributesCopy))
                            item!!.amount = item!!.amount - (Int.MAX_VALUE - amount)
                        } else {
                            set(slot, Item(id, item!!.amount + amount, item!!.attributesCopy))
                            item!!.amount = 0
                        }
                        result.succeededAmount = requestedAmount - item!!.amount
                        result.result =
                            if (result.succeededAmount != requestedAmount) RequestResult.OVERFLOW else RequestResult.SUCCESS
                        return result
                    }
                }
            }
            result.result = RequestResult.NOT_ENOUGH_SPACE
            return result
        }
        var slot = getSlotOf(id)
        if (item!!.hasAttributes() || (policy == ContainerPolicy.NEVER_STACK) || ((slot == -1) && stackable && (policy == ContainerPolicy.NORMAL)) || (slot == -1)) {
            if (policy != ContainerPolicy.ALWAYS_STACK && !stackable || policy == ContainerPolicy.NEVER_STACK) {
                val amt = availableSlots.size
                for (i in item!!.amount - 1 downTo 0) {
                    if (availableSlots.isEmpty() || containerSize - availableSlots.size >= storageSpaces) {
                        break
                    }
                    set(availableSlots.firstInt(), Item(item!!.id, 1, item!!.attributesCopy))
                }
                result.succeededAmount = Math.min(amt, requestedAmount)
                result.result = if (amt >= requestedAmount) RequestResult.SUCCESS else RequestResult.NOT_ENOUGH_SPACE
                return result
            }
            set(availableSlots.firstInt(), item)
            result.succeededAmount = requestedAmount
            result.result = RequestResult.SUCCESS
            return result
        }
        if (stackable || policy == ContainerPolicy.ALWAYS_STACK) {
            val existingItem = get(slot)
            val amount = existingItem?.amount ?: 0
            if (amount + item!!.amount < 0) {
                set(slot, Item(id, Int.MAX_VALUE, item!!.attributesCopy))
                item!!.amount = item!!.amount - (Int.MAX_VALUE - amount)
            } else {
                set(slot, Item(id, item!!.amount + amount, item!!.attributesCopy))
                item!!.amount = 0
            }
        } else {
            for (i in item!!.amount - 1 downTo 0) {
                if (availableSlots.isEmpty() || containerSize - availableSlots.size >= storageSpaces) {
                    result.succeededAmount = requestedAmount - item!!.amount
                    result.result = RequestResult.NOT_ENOUGH_SPACE
                    return result
                }
                val availableSlot = availableSlots.firstInt()
                set(availableSlot, Item(id, 1, item!!.attributesCopy))
                item!!.amount = item!!.amount - 1
            }
        }
        result.succeededAmount = requestedAmount - item!!.amount
        result.result = if (result.succeededAmount != requestedAmount) RequestResult.OVERFLOW else RequestResult.SUCCESS
        return result
    }

    override fun withdraw(player: Player, container: Container, slot: Int, amount: Int) {
        var slot = slot
        var amount = amount
        val item = get(slot)
        if (item == null || amount <= 0) {
            return
        }
        val definitions = item.definitions ?: return
        val stackable =
            definitions.isStackable() || !item.hasAttributes() && player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.notedId != -1 && ItemDefinitions.get(
                definitions.notedId
            ).isStackable()
        if (item.hasAttributes()) {
            slot = getSlotOf(item.id)
        }
        if (!item.hasAttributes() && stackable) {
            val existingAmount =
                if (player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.notedId != -1) container.getAmountOf(
                    definitions.notedId
                ) else container.getAmountOf(item.id)
            var notify = false
            if (existingAmount + amount < 0) {
                amount = Int.MAX_VALUE - existingAmount
                notify = true
            }
            if (existingAmount == 0) {
                val freeSlots = container.freeSlotsSize
                if (freeSlots == 0) {
                    amount = 0
                }
            }
            if (amount == 0 || notify) {
                player.sendMessage("Not enough space in your " + container.type.getName() + ".")
                if (amount == 0) {
                    return
                }
            }
            if (!item.hasAttributes() && player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.notedId == -1) {
                player.sendMessage("You can't withdraw this item as a note.")
            }
            val id =
                if (!item.hasAttributes() && player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && !definitions.isNoted && definitions.notedId != -1) definitions.notedId else item.id
            val containerResult = remove(Item(item.id, amount, item.attributesCopy))
            val result = containerResult.result
            if (result == RequestResult.FAILURE || containerResult.succeededAmount <= 0) {
                return
            }
            container.add(Item(id, containerResult.succeededAmount, item.attributesCopy))
        } else {
            val availableSpace = container.freeSlotsSize
            if (amount > availableSpace) {
                amount = availableSpace
            }
            if (amount == 0) {
                player.sendMessage("Not enough space in your " + container.type.getName() + ".")
                return
            }
            var i = if (amount == 1) slot else 0
            val length = if (amount == 1) slot + 1 else containerSize
            while (i < length) {
                if (amount <= 0) {
                    break
                }
                val containerItem = get(i)
                if (containerItem == null) {
                    i++
                    continue
                }
                if (containerItem.id != item.id) {
                    i++
                    continue
                }
                val containerResult = remove(i--, 1)
                val result = containerResult.result
                if (result == RequestResult.FAILURE || containerResult.succeededAmount <= 0) {
                    break
                }
                amount -= containerResult.succeededAmount
                if (player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.notedId == -1) {
                    player.sendMessage("You can't withdraw this item as a note.")
                }
                val id =
                    if (player.bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.isNoted && definitions.notedId != -1) definitions.notedId else item.id
                container.add(Item(id, containerResult.succeededAmount, containerItem.attributesCopy))
                i++
            }
        }
    }

    override fun remove(slot: Int, amount: Int): ContainerResult {
        val item = get(slot)
        return remove(Item(item?.id ?: -1, amount))
    }
}
