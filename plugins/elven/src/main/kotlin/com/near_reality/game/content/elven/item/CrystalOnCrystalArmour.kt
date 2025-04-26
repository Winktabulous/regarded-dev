package com.near_reality.game.content.elven.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project Near-Reality
 * @social Discord: Glabay
 * @since 2024-09-17
 */
@Suppress("UNUSED")
class CrystalOnCrystalArmour : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {

        val target = CorruptedRecolourCrystal.entries
            .find { it.crystalItemId == from.id || it.crystalItemId == to.id }
            ?: return

        val source = CorruptedRecolourCrystal.entries
            .find {
                it.helmId == from.id || it.helmId == to.id ||
                it.chestId == from.id || it.chestId == to.id ||
                it.legId == from.id || it.legId == to.id
            } ?: return

        val crystalItem = when (target.crystalItemId) {
            from.id -> from
            to.id -> to
            else -> error("Did not find crystal item id for $target")
        }

        val oldArmourItem = if (from.id == source.helmId || from.id == source.chestId || from.id == source.legId) from else to
        val newArmourItem = when (oldArmourItem.id) {
            source.helmId -> target.helmId
            source.chestId -> target.chestId
            source.legId -> target.legId
            else -> error("Invalid crystal weapon item id for $source")
        }
        val amourName = oldArmourItem.name
        player.dialogue {
            options("Are you sure you wish to recolour your $amourName?") {
                "Yes" {
                    val slot = if (oldArmourItem == from) fromSlot else toSlot
                    val itemAtSlot = player.inventory.getItem(slot)
                    require(itemAtSlot == oldArmourItem) {
                        "Expected $oldArmourItem at slot[$slot] but got $itemAtSlot"
                    }
                    if (player.inventory.deleteItem(crystalItem).result == RequestResult.SUCCESS) {
                        player.inventory.replaceItem(newArmourItem, 1, slot)
                        player.dialogue {
                            doubleItem(
                                newArmourItem,
                                target.crystalItemId,
                                "You use the crystal to colour your $amourName."
                            )
                        }
                    }
                }
                "No" {}
            }
        }
    }

    override fun getItems() = CorruptedRecolourCrystal.entries
        .flatMap { listOf(it.crystalItemId, it.helmId, it.chestId, it.legId) }
        .toSet()
        .toIntArray()

    private enum class CorruptedRecolourCrystal(
        val crystalItemId: Int,
        val helmId: Int,
        val chestId: Int,
        val legId: Int,
    ) {
        ITHELL(     ItemId.CRYSTAL_OF_ITHELL,       ItemId.CRYSTAL_HELM_27717,  ItemId.CRYSTAL_BODY_27709,  ItemId.CRYSTAL_LEGS_27713),
        IORWERTH(   ItemId.CRYSTAL_OF_IORWERTH,     ItemId.CRYSTAL_HELM_27729,  ItemId.CRYSTAL_BODY_27721,  ItemId.CRYSTAL_LEGS_27725),
        TRAHAEARN(  ItemId.CRYSTAL_OF_TRAHAEARN,    ItemId.CRYSTAL_HELM_27741,  ItemId.CRYSTAL_BODY_27733,  ItemId.CRYSTAL_LEGS_27737),
        CADARN(     ItemId.CRYSTAL_OF_CADARN,       ItemId.CRYSTAL_HELM_27753,  ItemId.CRYSTAL_BODY_27745,  ItemId.CRYSTAL_LEGS_27749),
        CRWYS(      ItemId.CRYSTAL_OF_CRWYS,        ItemId.CRYSTAL_HELM_27765,  ItemId.CRYSTAL_BODY_27757,  ItemId.CRYSTAL_LEGS_27761),
        MEILYR(     ItemId.CRYSTAL_OF_MEILYR,       ItemId.CRYSTAL_HELM,        ItemId.CRYSTAL_BODY,        ItemId.CRYSTAL_LEGS),
        HEFIN(      ItemId.CRYSTAL_OF_HEFIN,        ItemId.CRYSTAL_HELM_27705,  ItemId.CRYSTAL_BODY_27697,  ItemId.CRYSTAL_LEGS_27701),
        AMLODD(     ItemId.CRYSTAL_OF_AMLODD,       ItemId.CRYSTAL_HELM_27777,  ItemId.CRYSTAL_BODY_27769,  ItemId.CRYSTAL_LEGS_27773),
    }

}

