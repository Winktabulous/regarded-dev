package com.near_reality.game.content.araxxor.items.venom

import com.near_reality.game.content.seq
import com.zenyte.game.content.consumables.ConsumableAnimation
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.ARAXYTE_VENOM_SACK
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.item.pluginextensions.bindKt
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.variables.TickVariable

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-01
 */
class VenomSack : ItemPlugin() {
    override fun handle() {
        bindKt("Eat") {
            if (player.inventory.deleteItem(Item(ARAXYTE_VENOM_SACK, 1)).result == RequestResult.SUCCESS) {
                player seq ConsumableAnimation.EAT_ANIM.id
                player.applyHit(Hit(null, 4, HitType.VENOM))
                player.toxins.resetVenom()
                player.variables.schedule(1170, TickVariable.POISON_IMMUNITY)
                player.variables.schedule(18, TickVariable.VENOM_IMMUNITY)
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(ARAXYTE_VENOM_SACK)
}