package com.near_reality.game.content.wilderness.revenant.item

import com.near_reality.game.content.wilderness.revenant.ForinthrySurge
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.equipment.equip.EquipPlugin

/**
 * @author Kris | 22/04/2019 15:30
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("unused")
class AmuletOfAvariceEquipPlugin : EquipPlugin {

    override fun handle(player: Player, item: Item, slotId: Int, equipmentSlot: Int): Boolean {
        if (slotId != -1 && player.temporaryAttributes.remove("Wear avarice amulet") == null) {
            player.interfaceHandler.closeInterfaces()
            player.dialogue {
                options("Wearing this amulet will give you a PK skull.") {
                    dialogueOption("Give me a PK skull.", noPlayerMessage = true) {
                        player.temporaryAttributes["Wear avarice amulet"] = true
                        player.equipment.wear(slotId)
                    }
                    dialogueOption("Cancel.", noPlayerMessage = true)
                }
            }
            return false
        }
        return true
    }

    override fun onEquip(player: Player, container: Container, equippedItem: Item) {
        player.variables.setPermanentSkull()
    }

    override fun onUnequip(player: Player, container: Container, unequippedItem: Item) {
        if (ForinthrySurge.isActive(player))
            ForinthrySurge.deactivate(player)
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.AMULET_OF_AVARICE)
}
