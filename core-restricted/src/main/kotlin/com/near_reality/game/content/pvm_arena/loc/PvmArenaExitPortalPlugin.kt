package com.near_reality.game.content.pvm_arena.loc

import com.near_reality.game.content.pvm_arena.area.PvmArenaLobbyArea
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the exiting of the fight area of a PvM Arena.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaExitPortalPlugin : ObjectAction {

    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?,
    ) {
        player ?: return
        `object` ?: return
        player.dialogue {
            plain("Are you sure you wish to leave this area?<br>You will no longer receive any points for participating in the PvM Arena activity.")
            options("Leave this area?") {
                "Yes" {
                    PvmArenaLobbyArea.moveInto(player)
                }
                "No" {}
            }
        }
    }


    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.EXIT_PORTAL_27096)
}
