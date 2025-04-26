package com.near_reality.game.content.crystal

import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Developer commands for easy testing of crystal combat.
 *
 * @author Stan van der Bend.
 */
object CrystalCommands {

    fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "crystal") { p, args ->
            p.options {
                "materials" {
                    for (recipe in CrystalRecipe.all) {
                        if (recipe is CrystalChargeable) {
                            p.inventory.addItem(CRYSTAL_SHARD, recipe.crystalShardCost)
                            p.inventory.addItems(*recipe.materials.toTypedArray())
                        }
                    }
                }
                "recolor-crystals" {
                    p.inventory.addItems(
                        Item(ItemId.CRYSTAL_OF_ITHELL, 1),
                        Item(ItemId.CRYSTAL_OF_IORWERTH, 1),
                        Item(ItemId.CRYSTAL_OF_TRAHAEARN, 1),
                        Item(ItemId.CRYSTAL_OF_CADARN, 1),
                        Item(ItemId.CRYSTAL_OF_CRWYS, 1),
                        Item(ItemId.CRYSTAL_OF_MEILYR, 1),
                        Item(ItemId.CRYSTAL_OF_HEFIN, 1),
                        Item(ItemId.CRYSTAL_OF_AMLODD, 1),
                    )
                }
                "singing-bowl" {
                    p.teleport(Location(3238, 6066, 0))
                }
            }
        }
    }
}
