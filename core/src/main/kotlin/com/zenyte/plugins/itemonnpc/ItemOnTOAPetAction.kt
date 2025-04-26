package com.zenyte.plugins.itemonnpc

import com.near_reality.game.world.entity.player.*
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 */
@Suppress("unused")
class ItemOnTOAPetAction : ItemOnNPCAction {

    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        when(item.id) {
            ItemId.REMNANT_OF_BABA -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Babi!"))
                player.toaPetBabi = true
            }
            ItemId.REMNANT_OF_AKKHA -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Akkhito!"))
                player.toaPetAkkhito = true
            }
            ItemId.REMNANT_OF_KEPHRI -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Kephriti!"))
                player.toaPetKephriti = true
            }
            ItemId.REMNANT_OF_ZEBAK -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Zebo!"))
                player.toaPetZebo = true
            }
            ItemId.ANCIENT_REMNANT -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the damaged transformations of both Guardian's!"))
                player.toaPetRemnant = true
            }
        }
        player.inventory.deleteItem(item)
    }

    override fun getItems() = arrayOf(ItemId.REMNANT_OF_BABA, ItemId.REMNANT_OF_AKKHA, ItemId.REMNANT_OF_KEPHRI, ItemId.REMNANT_OF_ZEBAK, ItemId.ANCIENT_REMNANT)

    override fun getObjects() = arrayOf(
        NpcId.TUMEKENS_GUARDIAN,
        NpcId.TUMEKENS_DAMAGED_GUARDIAN,
        NpcId.ELIDINIS_DAMAGED_GUARDIAN,
        NpcId.ELIDINIS_GUARDIAN,
        NpcId.AKKHITO,
        NpcId.BABI,
        NpcId.KEPHRITI,
        NpcId.ZEBO
    )
}