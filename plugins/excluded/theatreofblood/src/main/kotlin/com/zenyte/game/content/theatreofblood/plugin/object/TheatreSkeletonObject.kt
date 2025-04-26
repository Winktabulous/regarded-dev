package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.start
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class TheatreSkeletonObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val area = VerSinhazaArea.getArea(player) ?: return
        if (!area.completed) {
            player.sendMessage("You can't search this until the challenge is complete.")
            return
        }

        val dawnbringer = Item(ItemId.DAWNBRINGER, 1)
        if (player.inventory.addItem(dawnbringer).isFailure)
            player.sendMessage("You don't have enough inventory space to take the Dawnbringer.")
        else {
            World.replaceObject(obj, obj.transform(ObjectId.SKELETON_32742))
            player.dialogueManager.start {
                item(dawnbringer, "You find the Dawnbringer; you feel a pulse of energy<br>burst through it.")
            }
        }
    }

    override fun getObjects() = TheatreSkeletonObject.objects

    private companion object {

        val objects = arrayOf(ObjectId.SKELETON_32741)

    }

}