package com.near_reality.game.content.dt2.plugins.objects.duke

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

@Suppress("unused")
class FermentationVatCollectObject : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.collectPoison(`object`)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(47538)
    }

    private fun Player.collectPoison(loc: WorldObject) {
        if (!inventory.hasFreeSlots()) {
            dialogue {
                plain("Your inventory is too full to carry any more.")
            }
            return
        }

        val replacement = WorldObject(47536, loc.type, loc.rotation, loc.position)
        World.spawnObject(replacement)
        inventory.addItem(Item(ItemId.ARDERMUSCA_POISON))
    }
}
