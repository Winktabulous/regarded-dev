package com.near_reality.game.content.elven.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles the re-colouring of corrupted crystal weapons.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class CrystalOnCorruptedWeapon : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {

        val target = CorruptedRecolourCrystal.values()
            .find { it.crystalItemId == from.id || it.crystalItemId == to.id }
            ?: return

        val source = CorruptedRecolourCrystal.values()
            .find {
                it.bladeItemId == from.id || it.bladeItemId == to.id ||
                        it.bowItemId == from.id || it.bowItemId == to.id
            } ?: return

        val crystalItem = when (target.crystalItemId) {
            from.id -> from
            to.id -> to
            else -> error("Did not find crystal item id for $target")
        }

        val oldWeaponItem = if (from.id == source.bowItemId || from.id == source.bladeItemId) from else to
        val newWeaponId = when (oldWeaponItem.id) {
            source.bladeItemId -> target.bladeItemId
            source.bowItemId -> target.bowItemId
            else -> error("Invalid crystal weapon item id for $source")
        }
        val weaponName = oldWeaponItem.name
        player.dialogue {
            options("Are you sure you wish to recolour your $weaponName?") {
                "Yes" {
                    val slot = if (oldWeaponItem == from) fromSlot else toSlot
                    val itemAtSlot = player.inventory.getItem(slot)
                    require(itemAtSlot == oldWeaponItem) {
                        "Expected $oldWeaponItem at slot[$slot] but got $itemAtSlot"
                    }
                    if (player.inventory.deleteItem(crystalItem).result == RequestResult.SUCCESS) {
                        player.inventory.replaceItem(newWeaponId, 1, slot)
                        player.dialogue {
                            doubleItem(
                                newWeaponId,
                                target.crystalItemId,
                                "You use the crystal to colour your $weaponName."
                            )
                        }
                    }
                }
                "No" {}
            }
        }
    }

    override fun getItems() = CorruptedRecolourCrystal.values()
        .flatMap { listOf(it.crystalItemId, it.bladeItemId, it.bowItemId) }
        .toSet()
        .toIntArray()

    private enum class CorruptedRecolourCrystal(
        val crystalItemId: Int,
        val bladeItemId: Int,
        val bowItemId: Int,
    ) {
        ITHELL(ItemId.CRYSTAL_OF_ITHELL, ItemId.BLADE_OF_SAELDOR_C_25870, ItemId.BOW_OF_FAERDHINEN_C_25884),
        IORWERTH(ItemId.CRYSTAL_OF_IORWERTH, ItemId.BLADE_OF_SAELDOR_C_25872, ItemId.BOW_OF_FAERDHINEN_C_25886),
        TRAHAEARN(ItemId.CRYSTAL_OF_TRAHAEARN, ItemId.BLADE_OF_SAELDOR_C_25874, ItemId.BOW_OF_FAERDHINEN_C_25888),
        CADARN(ItemId.CRYSTAL_OF_CADARN, ItemId.BLADE_OF_SAELDOR_C_25876, ItemId.BOW_OF_FAERDHINEN_C_25890),
        CRWYS(ItemId.CRYSTAL_OF_CRWYS, ItemId.BLADE_OF_SAELDOR_C_25878, ItemId.BOW_OF_FAERDHINEN_C_25892),
        MEILYR(ItemId.CRYSTAL_OF_MEILYR, ItemId.BLADE_OF_SAELDOR_C_25880, ItemId.BOW_OF_FAERDHINEN_C_25894),
        HEFIN(ItemId.CRYSTAL_OF_HEFIN, ItemId.BLADE_OF_SAELDOR_C, ItemId.BOW_OF_FAERDHINEN_C),
        AMLODD(ItemId.CRYSTAL_OF_AMLODD, ItemId.BLADE_OF_SAELDOR_C_25882, ItemId.BOW_OF_FAERDHINEN_C_25896),
    }

}

