package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.player.accessSharedStorage
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.testinterfaces.BankInventoryInterface
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*

class StorageGimInventoryInterface : InterfaceScript() {
    init {
        STORAGE_GIM_INVENTORY {
            val container = "Interact with Item"(0) {
                player.accessSharedStorage {
                    depositFromInventory(player, slotID, itemID, BankInventoryInterface.ItemOption.ofShared(option))
                }
            }
            drag(container, container) {
                player.accessSharedStorage {
                    switchInInventory(player, fromSlotID, toSlotID)
                }
            }
            opened {
                if (!interfaceHandler.isPresent(STORAGE_GIM))
                    throw RuntimeException("Group Storage inventory overlay cannot be opened without the presence of storage itself.")
                val inventorySize = Container.getSize(ContainerType.GIM_INVENTORY)
                packetDispatcher.sendComponentSettings(
                    STORAGE_GIM_INVENTORY,
                    getComponent("Interact with Item"),
                    0,
                    inventorySize,
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
                    DRAG_DEPTH1,
                    DRAG_TARGETABLE
                )
                packetDispatcher.sendUpdateItemContainer(inventory.container, ContainerType.GIM_INVENTORY)
                interfaceHandler.sendInterface(getInterface())
            }
        }
    }
}
