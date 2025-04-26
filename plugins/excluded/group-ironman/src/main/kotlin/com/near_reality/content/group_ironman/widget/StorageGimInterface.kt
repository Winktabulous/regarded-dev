package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.IronmanGroupStorage
import com.near_reality.content.group_ironman.checkUnlockedSpaces
import com.near_reality.content.group_ironman.getUnlockedSpaces
import com.near_reality.content.group_ironman.player.accessSharedStorage
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.testinterfaces.BankInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.container.impl.bank.Bank
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting
import com.zenyte.game.world.entity.player.dialogue.loadingDialogueOnWorldThread
import com.zenyte.utils.TextUtils
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*

class StorageGimInterface : InterfaceScript() {
    init {
        STORAGE_GIM {
            val container = "Interact with item"(10) {
                player.accessSharedStorage {
                    withdrawFromBank(player, slotID, itemID, IronmanGroupStorage.BankOption.of(option))
                }
            }
            drag(container, container) {
                player.accessSharedStorage {
                    switchInBank(player, fromSlotID, toSlotID)
                }
            }
            "Back to bank"(9) {
                player.save {
                    BANK.open(player)
                }
            }
            "Storage Unlocks"(12) {
                SETTINGS_GIM_STORAGE.open(player)
            }
            "Shuffle items up"(14) {
                player.accessSharedStorage {
                    shuffleItems(player)
                }
            }
            "Search"(16) {
            }
            "Deposit inventory"(18) {
                player.accessSharedStorage {
                    depositEntireInventory(player)
                }
            }
            "Rearrange mode: Swap"(23) {
                player.bank.toggleSetting(BankSetting.REARRANGE_MODE, false)
            }
            "Rearrange mode: Insert"(25) {
                player.bank.toggleSetting(BankSetting.REARRANGE_MODE, true)
            }
            "Withdraw as: Item"(29) {
                player.bank.toggleSetting(BankSetting.WITHDRAW_MODE, false)
            }
            "Withdraw as: Note"(31) {
                player.bank.toggleSetting(BankSetting.WITHDRAW_MODE, true)
            }
            "Save"(34) {
                player.save()
            }
            "Quantity: 1"(37) {
                player.bank.quantity = Bank.QuantitySelector.ONE
            }
            "Quantity: 5"(39) {
                player.bank.quantity = Bank.QuantitySelector.FIVE
            }
            "Quantity: 10"(41) {
                player.bank.quantity = Bank.QuantitySelector.TEN
            }
            "Quantity: X"(43) {
                if (option == 2) {
                    player.sendInputInt("Set quantity selector amount:") { a: Int ->
                        val value = a.coerceAtLeast(1)
                        player.bank.lastDepositAmount = value
                        player.varManager.sendBit(
                            BankInterface.VAR_LAST_DEPOSIT_AMOUNT,
                            player.bank.lastDepositAmount
                        )
                    }
                    if (player.bank.lastDepositAmount == 0) {
                        player.varManager.sendBit(BankInterface.VAR_LAST_DEPOSIT_AMOUNT, 1)
                    }
                }
                player.bank.quantity = Bank.QuantitySelector.X
            }
            "Quantity: All"(45) {
                player.bank.quantity = Bank.QuantitySelector.ALL
            }
            closed {
                save()
            }
            opened {
                val group = finalisedIronmanGroup
                if (group == null){
                    sendDeveloperMessage("Could not open shared storage, not in group")
                    return@opened
                }
                loadingDialogueOnWorldThread("Loading group storage...") {
                    val member = group.findMember(this)
                    if (member == null) {
                        sendDeveloperMessage("Could not open shared storage, not a member of group ${group.name}")
                        return@loadingDialogueOnWorldThread
                    }
                    group.sharedStorage.tryGainAccess(member,
                        onSuccess = {
                            group.checkUnlockedSpaces(this)
                            val spaces = group.getUnlockedSpaces()
                            group.sharedStorage.bank.storageSpaces = spaces
                            varManager.sendVarInstant(261, -1)//price cap maybe?
                            varManager.sendVarInstant(262, 3)//cap to view, deposit only?
                            varManager.sendVarInstant(263, spaces)
                            varManager.sendVarInstant(264, 1)//whether can return to bank: 0,1
                            varManager.sendBit(BankInterface.VAR_LAST_DEPOSIT_AMOUNT, bank.lastDepositAmount)
                            packetDispatcher.sendComponentSettings(
                                `interface`,
                                getComponent("Interact with item"),
                                0,
                                Container.getSize(ContainerType.GIM_BANK),
                                CLICK_OP1,
                                CLICK_OP2,
                                CLICK_OP3,
                                CLICK_OP4,
                                CLICK_OP5,
                                CLICK_OP6,
                                CLICK_OP7,
                                CLICK_OP8,
                                CLICK_OP9,
                                CLICK_OP10,
                                DRAG_DEPTH2,
                                DRAG_TARGETABLE
                            )
                            packetDispatcher.sendUpdateItemContainer(group.sharedStorage.bank, ContainerType.GIM_BANK)
                            sendInterface()
                            STORAGE_GIM_INVENTORY.open(this)
                        },
                        onAlreadyAccessed = { other ->
                            sendMessage("Your group's data is currently in use by ${TextUtils.formatName(other.username)}")
                            BANK.open(this)
                        }
                    )
                }
            }
        }
    }

    fun Player.save(onSaved: () -> Unit = {}) {
        val group = finalisedIronmanGroup
        if (group == null) {
            sendDeveloperMessage("Could not save shared storage, not in group")
            return
        }
        loadingDialogueOnWorldThread("Saving...") {
            group.sharedStorage.relinquishAccess()
            onSaved()
        }
    }
}
