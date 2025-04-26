package com.near_reality.game.content.pvm_arena.loc

import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the portal objects that players can use to join a team in the PvM Arena.
 *
 * @author Stan van der Bend
 */
class PvmArenaTeamPortalPlugin : ObjectAction {

    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?,
    ) {
        player ?: return
        `object` ?: return
        val team = when (`object`.id) {
            TEAM_BLUE_PORTAL_ID -> PvmArenaTeam.Blue
            TEAM_RED_PORTAL_ID -> PvmArenaTeam.Red
            else -> return
        }
        PvmArenaManager.tryJoinTeam(player, team)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(TEAM_BLUE_PORTAL_ID, TEAM_RED_PORTAL_ID)

    companion object {
        const val TEAM_BLUE_PORTAL_ID = 43765
        const val TEAM_RED_PORTAL_ID = 43767
    }
}
