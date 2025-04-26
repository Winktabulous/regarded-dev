package com.near_reality.content.group_ironman

import com.near_reality.content.group_ironman.player.accessSharedStorage
import com.zenyte.game.model.ui.testinterfaces.BankInterface
import com.zenyte.game.model.ui.testinterfaces.BankInventoryInterface
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting

/**
 * Group ironman players have access to their group's shared storage, available via the regular Bank interface.
 * All players in the group can deposit into the shared storage, but only one player can access it at a time.
 *
 * @author Stan van der Bend
 */
class IronmanGroupStorage {

    /**
     * The [member][IronmanGroupMember] currently having withdrawal access to the storage,
     * or `null` in case no member is currently accessing it.
     */
    @Transient
    private var accessingMember: IronmanGroupMember? = null

    /**
     * The [IronmanGroupStorageContainer] used for shared storage by members of a [IronmanGroup].
     */
    internal var bank = IronmanGroupStorageContainer()

    fun tryGainAccess(member: IronmanGroupMember, onSuccess: () -> Unit, onAlreadyAccessed: (IronmanGroupMember) -> Unit) {
        val currentMember = accessingMember
        if (currentMember?.isOnline() == true)
            onAlreadyAccessed(currentMember)
        else {
            accessingMember = member
            member.ifOnline {
                sendDeveloperMessage("You gained access over the shared storage.")
            }
            onSuccess()
        }
    }

    fun relinquishAccess() {
        accessingMember?.run {
            ifOnline {
                sendDeveloperMessage("You relinquished access over the shared storage.")
            }
        }
        accessingMember = null
    }

    fun shuffleItems(player: Player) {
        bank.shift()
        updateStorageInventory(player)
        updateStorageBank(player)
        IronmanGroupModule.requestSave()
    }

    fun depositEntireInventory(player: Player) {
        player.inventory.container.run {
            for (slot in 0 until containerSize) {
                val item = this[slot] ?: continue
                if (!item.isTradable) continue
                bank.deposit(null, this, slot, item.amount)
            }
        }
        updateStorageInventory(player)
        updateStorageBank(player)
        IronmanGroupModule.requestSave()
    }

    fun depositFromInventory(player: Player, slotID: Int, itemID: Int, option: BankInventoryInterface.ItemOption) {
        if (player.inventory.getId(slotID) != itemID) {
            player.sendDeveloperMessage("There is no item with id $itemID at slot $slotID in the inventory.")
            return
        }
        if (!player.inventory.container.get(slotID).isTradable) {
            player.sendMessage("You can't store that.")
            return
        }
        when (option) {
            BankInventoryInterface.ItemOption.EXAMINE -> {
                ItemUtil.sendItemExamine(player, itemID)
            }
            BankInventoryInterface.ItemOption.WITHDRAW_X -> {
                player.sendInputInt("How many would you like to deposit?") { amount: Int ->
                    player.accessSharedStorage {
                        player.bank.lastDepositAmount = amount
                        player.varManager.sendBit(BankInterface.VAR_LAST_DEPOSIT_AMOUNT, player.bank.lastDepositAmount)
                        bank.deposit(player, player.inventory.container, slotID, amount)
                        updateStorageInventory(player)
                        updateStorageBank(player)
                        IronmanGroupModule.requestSave()
                    }

                }
            }
            else -> {
                val amount = when (option) {
                    BankInventoryInterface.ItemOption.WITHDRAW_1_OR_SELECTED -> player.bank.currentQuantity
                    BankInventoryInterface.ItemOption.WITHDRAW_LAST_AMOUNT -> player.bank.lastDepositAmount
                    else -> option.amount
                }
                bank.deposit(player, player.inventory.container, slotID, amount)
                updateStorageInventory(player)
                updateStorageBank(player)
                IronmanGroupModule.requestSave()
            }
        }
    }

    fun switchInInventory(player: Player, fromSlotID: Int, toSlotID: Int) {
        if (!checkSize(player, toSlotID, fromSlotID, ContainerType.GIM_INVENTORY))
            return
        player.inventory.switchItem(fromSlotID, toSlotID)
        updateStorageInventory(player)
        IronmanGroupModule.requestSave()
    }

    fun switchInBank(player: Player, fromSlotID: Int, toSlotID: Int) {
        if (!checkSize(player, toSlotID, fromSlotID, ContainerType.GIM_BANK))
            return

        val rearrangeMode = player.bank.getSetting(BankSetting.REARRANGE_MODE)
        bank.switchItem(fromSlotID, toSlotID, rearrangeMode == 1)
        updateStorageBank(player)
        IronmanGroupModule.requestSave()
    }

    private fun checkSize(player: Player, toSlotID: Int, fromSlotID: Int, containerType: ContainerType) : Boolean {
        val size = Container.getSize(containerType)
        if (fromSlotID !in 0..size || toSlotID !in 0..size) {
            player.sendDeveloperMessage("fromSlotID (=$fromSlotID) and/or toSlotID (=$toSlotID) is not in range 0..${size}")
            return false
        }
        return true
    }

    fun withdrawFromBank(player: Player, slotID: Int, itemID: Int, option: BankOption) {
        if (bank.get(slotID)?.id != itemID) {
            player.sendDeveloperMessage("There is no item with id $itemID at slot $slotID in the shared storage.")
            return
        }
        when(option) {
            BankOption.Examine -> ItemUtil.sendItemExamine(player, itemID)
            BankOption.WithdrawX -> {
                player.sendInputInt("How many would you like to withdraw?") { amount: Int ->
                    val itemAmount: Int = bank.getAmountOf(itemID)
                    player.bank.lastDepositAmount = amount
                    player.varManager.sendBit(BankInterface.VAR_LAST_DEPOSIT_AMOUNT, player.bank.lastDepositAmount)
                    bank.withdraw(
                        player,
                        player.inventory.container,
                        slotID,
                        itemAmount.coerceAtMost(amount)
                    )
                    updateStorageInventory(player)
                    updateStorageBank(player)
                    IronmanGroupModule.requestSave()
                }
            }
            else -> {
                val itemAmount: Int = bank.getAmountOf(itemID)
                val amount = option.amountProvider!!(player, itemAmount)
                bank.withdraw(
                    player,
                    player.inventory.container,
                    slotID,
                    itemAmount.coerceAtMost(amount)
                )
                updateStorageInventory(player)
                updateStorageBank(player)
                IronmanGroupModule.requestSave()
            }
        }
    }

    private fun updateStorageBank(player: Player) {
        player.packetDispatcher.sendUpdateItemContainer(bank, ContainerType.GIM_BANK)
    }

    private fun updateStorageInventory(player: Player) {
        player.packetDispatcher.sendUpdateItemContainer(
            player.inventory.container,
            ContainerType.GIM_INVENTORY
        )
        player.inventory.refresh()
    }

    sealed class BankOption(val amountProvider: ((Player, Int) -> Int)? = null) {

        object WithdrawDynamic: BankOption(amountProvider = { player, _ -> player.bank.currentQuantity })
        object Withdraw1 : BankOption(amountProvider = 1.provide())
        object Withdraw5 : BankOption(amountProvider = 5.provide())
        object Withdraw10 : BankOption(amountProvider = 10.provide())
        object WithdrawLastAmount : BankOption(amountProvider = { player, _ -> player.bank.lastDepositAmount })
        object WithdrawX : BankOption(amountProvider = { _, amount -> amount })
        object WithdrawAll : BankOption(amountProvider = { _, amount -> amount })
        object WithdrawAllBut1 : BankOption(amountProvider = { _, amount -> amount - 1 })
        object Examine : BankOption()

        companion object {
            fun of(option: Int) = when (option) {
                1 -> WithdrawDynamic
                2 -> Withdraw1
                3 -> Withdraw5
                4 -> Withdraw10
                5 -> WithdrawLastAmount
                6 -> WithdrawX
                7 -> WithdrawAll
                8 -> WithdrawAllBut1
                10 -> Examine
                else -> error("Unknown option $option")
            }

            fun Int.provide(): (Player, Int) -> Int = { _, _ -> this }
        }
    }
}
