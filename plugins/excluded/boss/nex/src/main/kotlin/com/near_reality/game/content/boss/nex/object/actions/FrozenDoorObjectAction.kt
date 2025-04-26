package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId

class FrozenDoorObjectAction : ObjectActionScript() {

    init {
        ObjectId.FROZEN_DOOR.handleFrozenDoor()
        ObjectId.FROZEN_DOOR_42932.handleFrozenDoor()
        ObjectId.FROZEN_DOOR_42931.handleFrozenDoor()
    }

    fun Int.handleFrozenDoor() = invoke {
        if (!DeveloperCommands.enabledNex) {
            player.dialogue { plain("Nex is currently disabled.") }
        } else {
            FadeScreen(player) {
                player.teleport(player.targetLocation())
                WorldTasksManager.scheduleOrExecute({
                    player.interfaceHandler.sendInterface(InterfacePosition.OVERLAY, 406)
                }, 1)
            }.fade(2)
        }
    }

    private val ancientLocation = Location(2856, 5227, 0)
    private val godwarsLocation = Location(2883, 5280, 2)

    fun Player.targetLocation() = if (ancientLocation.getDistance(location) > 10)
        ancientLocation
    else
        godwarsLocation

}


