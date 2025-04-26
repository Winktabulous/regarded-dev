package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.RenderAnimation
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal data class JailLocation(
    val localX: Int,
    val localY: Int,
    val faceDirection: Direction = Direction.SOUTH,
    val plane: Int = 0
) {

    fun sendPlayer(player: Player, raid: TheatreOfBloodRaid): Boolean {
        val area = VerSinhazaArea.getArea(player) ?: return false

        player.temporaryAttributes["tob_jailed"] = this
        player.lock()
        player.setLocation(area.getBaseLocation(localX, localY, plane))
        for (closeTab in closeTabs)
            player.interfaceHandler.closeInterface(closeTab)
        WorldTasksManager.schedule {
            player.faceDirection(faceDirection)
            player.appearance.renderAnimation = deathStallRenderAnim
        }

        for (p in raid.party.players) {
            if (!p.temporaryAttributes.containsKey("tob_jailed")) {
                return true
            }
        }

        WorldTasksManager.schedule({
            if (!raid.completed && !raid.failed) {
                raid.activityFailed()
            }
        }, 3)
        return true
    }

    companion object {

        private val deathStallRenderAnim = RenderAnimation(8070)

        private val closeTabs = arrayOf(GameInterface.INVENTORY_TAB, GameInterface.EQUIPMENT_TAB)

        fun restorePlayer(player: Player): Boolean {
            player.temporaryAttributes.remove("tob_jailed") ?: return false

            restorePlayerInner(player)
            return true
        }

        fun restorePlayerInner(player: Player): Boolean {
            player.unlock()
            player.appearance.resetRenderAnimation()
            player.animation = Animation.STOP
            for (closeTab in closeTabs)
                closeTab.open(player)
            return true
        }

    }

}