package com.near_reality.game.content.elven.obj

import com.near_reality.game.content.crystal.CrystalRecipe
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the Singing bowl object interactions,
 * players can use the bowl to craft new crystal items by following a [recipe][CrystalRecipe].
 *
 * https://oldschool.runescape.wiki/w/Singing_bowl
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class SingingBowl : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        if (option == "Sing-crystal")
            player.dialogueManager.start(SingCrystalDialogue(player))
    }

    override fun getObjects() = arrayOf(ObjectId.SINGING_BOWL_36552)
}

