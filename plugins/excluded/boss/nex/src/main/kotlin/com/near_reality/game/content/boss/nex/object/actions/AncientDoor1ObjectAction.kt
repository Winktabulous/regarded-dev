package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.Door
import com.zenyte.game.world.`object`.ObjectId

class AncientDoor1ObjectAction : ObjectActionScript() {
    init {
        ObjectId.DOOR_42933 {
            if (!DeveloperCommands.enabledNex) {
                player.dialogue { plain("Nex is currently disabled.") }
            } else {
                obj.isLocked = true
                val destX = player.x + (if (player.x == 2861) +2 else -2)
                player.sendDeveloperMessage("${player.x}, ${obj.x} $destX")
                player.resetWalkSteps()
                player.addWalkSteps(destX, player.y, 2, false)
                val door = Door.handleGraphicalDoor(obj, null)
                WorldTasksManager.schedule({
                    Door.handleGraphicalDoor(door, obj)
                    obj.isLocked = false
                }, 1)
            }
        }

        provideRouteEvent {
            if (player.x <= 2861)
                TileEvent(player, TileStrategy(Location(2861, 5219)), it)
            else
                TileEvent(player, TileStrategy(Location(2863, 5219)), it)
        }

    }
}
