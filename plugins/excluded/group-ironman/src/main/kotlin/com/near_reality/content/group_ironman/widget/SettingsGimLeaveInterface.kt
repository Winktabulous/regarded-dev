package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.content.group_ironman.player.leaveGroup
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player

class SettingsGimLeaveInterface : InterfaceScript() {
    init {
        GameInterface.SETTINGS_GIM_LEAVE {
            "No"(7) {}
            "Yes"(8) {}
            opened {
                val group = finalisedIronmanGroup
                if (group == null) {
                    sendDeveloperMessage("You cannot leave the group because you are not in one.")
                } else {
                    awaitInputInt { option ->
                        if (option == 1)
                            leaveGroup()
                    }
                    interfaceHandler.sendInterface(id, 21, PaneType.IRON_GROUP_SETTINGS, true)
                    updateGimSettingsLeave()
                }
            }
        }
    }

    fun Player.updateGimSettingsLeave() = packetDispatcher.run {
        val group = finalisedIronmanGroup ?: return@run
        sendClientScript(
            4212,
            if (group.isDeparting(this@updateGimSettingsLeave))
                buildLeavePeriodText()
            else
                buildLeaveText(group.isLeader(this@updateGimSettingsLeave)),
            730 component 21
        )
    }


    fun buildLeavePeriodText() = buildString {
        append("Really cancel your leave request?")
        append("|If you decide to leave again the ${IronmanGroup.gracePeriod.inWholeDays} day grace period will restart.")
        append("|No, don't cancel the request.")
        append("|<col=ff0000>Yes, cancel the request.</col>")
    }

    fun buildLeaveText(isLeader: Boolean) = buildString {
        append("Really leave the group?|Once you have left, you <col=ff0000>cannot rejoin</col>.<br><br>")
        append("Leaving the group will take ${IronmanGroup.gracePeriod.inWholeDays} days to take effect at which point you will be returned to the Node ")
        append("with the choice to de-iron or form a new Iron Group with fresh accounts.<br><br>")
        if (isLeader) {
            append("As you are the group's leader leaving will also cause you to abdicate, passing the leadership to someone else in the group. ")
            append("However, if no one is eligible when this is processed your leave request will be cancelled.<br><br>")
        }
        append("Are you sure you wish to leave?")
        append("|No don't leave.")
        append("|<col=ff0000>Yes leave the group.</col>")
    }

}
