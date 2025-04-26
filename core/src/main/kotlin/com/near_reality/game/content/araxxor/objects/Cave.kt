package com.near_reality.game.content.araxxor.objects

import com.near_reality.game.content.North
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.CAVE_42594
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-23
 */
class Cave : ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return
        `object` ?: return
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(Location(3679, 9797))
            player.faceDirection(Direction.NORTH)
        }
        screen.fade()
        player.animation = Animation.CRAWL
        schedule(1) {
            player.forceMovement = ForceMovement(Location(3658, 3409), 30, North.direction)
        }
        schedule(2) {
            screen.unfade()
            player.unlock()
        }

    }

    override fun getObjects(): Array<Any> = arrayOf(CAVE_42594)
}