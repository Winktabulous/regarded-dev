package com.near_reality.game.content.dt2.area.obj

import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.theduke.FermentationVatEntity
import com.zenyte.game.item.Item
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.WorldObject

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class FermentationVat(
    val arena: DukeSucellusInstance,
    private val ingredients: List<Item> = listOf(Item(28342, 0), Item(28346, 0), Item(28349, 0))
) {

    var vatEntity : FermentationVatEntity? = null
    var filling: Boolean = false

    fun fill(player: Player, loc: WorldObject) {
        if(filling) {
            player.sendMessage("This vat is currently fermenting. Please wait until it's completed.")
            return
        }
        val missingIngredients = ingredients.asSequence()
            .onEach {
                val requiredRemainder = INGREDIENTS_COUNT_REQ - it.amount
                if (requiredRemainder > 0) {
                    val added = player.inventory.deleteItem(it.id, requiredRemainder).succeededAmount
                    if (added > 0) {
                        it.amount += added
                    }
                }
            }
            .filter { it.amount < INGREDIENTS_COUNT_REQ }
            .toList()

        if (missingIngredients.isEmpty()) {
            filling = true
            vatEntity = FermentationVatEntity(loc.position)
            (vatEntity as FermentationVatEntity).beginFermentation(
                finishTask = {
                    val collectVat = WorldObject(47538, loc.type, loc.rotation, loc.position)
                    World.spawnObject(collectVat)
                    filling = false
                }
            )
            for(item in ingredients)
                item.amount = 0
        }
    }

    fun check(player: Player) {
        val info = ingredients.asSequence()
            .map {
                "${it.name}: ${it.amount} / $INGREDIENTS_COUNT_REQ<br>"
            }.joinToString("")
        player.dialogue{
            plain(info)
        }
    }

    companion object {

        private const val INGREDIENTS_COUNT_REQ = 6

    }
}
