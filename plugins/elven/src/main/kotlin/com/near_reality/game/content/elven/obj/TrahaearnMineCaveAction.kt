package com.near_reality.game.content.elven.obj

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the entrance and exit of the Trahaearn mine.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class TrahaearnMineCaveAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        when(option) {
            "Enter" -> {
                val screen = FadeScreen(player) { player.setLocation(Location(3302, 12454)) }
                screen.fade()
                player.animation = Animation(2796)
                player.sendSound(SoundEffect(2454, 0, 4))
                WorldTasksManager.schedule({ screen.unfade() }, 2)
            }
            "Exit" -> {
                val screen = FadeScreen(player) { player.setLocation(Location(3271, 6051)) }
                screen.fade()
                player.animation = Animation.LADDER_UP
                WorldTasksManager.schedule({ screen.unfade() }, 2)
            }
        }
    }

    override fun getObjects() = arrayOf(ObjectId.CAVE_ENTRANCE_36556, ObjectId.STEPS_36215)
}
