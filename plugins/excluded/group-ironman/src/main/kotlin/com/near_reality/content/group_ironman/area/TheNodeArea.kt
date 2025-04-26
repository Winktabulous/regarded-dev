package com.near_reality.content.group_ironman.area

import com.near_reality.content.group_ironman.npc.R0ck5masher
import com.near_reality.content.group_ironman.npc.Regent
import com.near_reality.content.group_ironman.player.trySetApplyOrInvitePlayerOption
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin
import com.zenyte.game.world.region.area.plugins.TeleportPlugin

/**
 * The Node is a group of 3 small islands just south of Tutorial Island,
 * connected to each other via bridges, only accessible to Group Ironman players.
 *
 * Here players can talk to the Group Ironman and Group Storage tutors to get information about Group Ironman,
 * and also setup or invite other players to their groups.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class TheNodeArea : PolygonRegionArea(), CycleProcessPlugin, TeleportPlugin {

    private var ticks = 6
    private val regent = Regent()
    private val rockSmasher = R0ck5masher(regent)

    override fun process() {
        if (regent.isFinished) {
            regent.spawn()
        }
        if (rockSmasher.isFinished) {
            rockSmasher.spawn()
            rockSmasher.handleSequence()
        }
        /*
         * RS does this for some reason, guess a fail-safe?
         */
        if (--ticks <= 0) {
            ticks = 6
            players.forEach {
               it.trySetApplyOrInvitePlayerOption()
            }
        }
    }

    override fun enter(player: Player) {
        WorldTasksManager.schedule({
            player.dialogue {
                item(
                    Item(22580, 400),
                    "Welcome to The Node! If you need some help, simply " +
                            "talk to the Iron Man Tutor. Look for this icon on your " +
                            "minimap to find him."
                )
            }
        }, 1)
    }

    override fun leave(player: Player, logout: Boolean) {
        player.clearTopLevelRowPlayerOptions()
    }

    override fun name(): String =
        "The Node"

    override fun polygons() =
        arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(3087, 3047), intArrayOf(3087, 3007),
                    intArrayOf(3122, 3007), intArrayOf(3122, 3047)
                )
            )
        )

    override fun canTeleport(player: Player, teleport: Teleport): Boolean {
        player.sendMessage("You can't teleport from The Node.")
        return false
    }

}
