package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.IronmanGroupType
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.game.util.invoke
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.util.Colour
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.TextUtils
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.*

class SettingsGimInterface : InterfaceScript() {
    init {
        GameInterface.SETTINGS_GIM {
            "Hiscore"(16) {
                //player.packetDispatcher.sendClientScript(1081, "https://secure.runescape.com/m=hiscore_oldschool_ironman/group-ironman/view-group?name=nrpk", 1, 1)
            }
            "Group Storage"(17) {
                GameInterface.SETTINGS_GIM_STORAGE.open(player)
            }
            "Options"(18) {
                GameInterface.SETTINGS_GIM_OPTIONS.open(player)
            }
            "Raffle"(19) {
                GameInterface.SETTINGS_GIM_RAFFLE.open(player)
            }
            "Leave Group"(20) {
                GameInterface.SETTINGS_GIM_LEAVE.open(player)
            }
            opened {
                val group = finalisedIronmanGroup
                if (group == null) {
                    sendDeveloperMessage("Could not open settings interface because you're not in an ironman group.")
                } else {
                    sendInterface()
                    updateGimSettings(group, this@SETTINGS_GIM)
                    if (group.isKicked(this)) {
                        sendMessage(Colour.TURQOISE("Your group leader has chosen to kick you from the Group Ironman group. "))
                    }
                }
            }
        }
    }

    fun Player.updateGimSettings(
        group: IronmanGroup,
        interfaceScript: InterfaceScript,
    ) = packetDispatcher.run {
        sendClientScript(5508,
            "Group Status:<br>Players in Group:<br>Group Prestige:<br>Leader:<br>Creation Date:",
            buildString {
                appendColouredLine(
                    when (group.type) {
                        IronmanGroupType.NORMAL -> "Iron Group"
                        IronmanGroupType.HARDCORE -> "Hardcore Iron Group"
                    }
                )
                appendColouredLine(group.allMembers.size.toString())
                appendColouredLine(if (group.ranked) "Prestiged" else "None")
                appendColouredLine(TextUtils.formatName(group.leaderUsername))
                appendColouredLine(buildString {
                    val creationTime = group.creationTime.toLocalDateTime(TimeZone.currentSystemDefault())
                    append("<col=ffb83f>")
                    append(creationTime.dayOfMonth)
                    append('-')
                    append(creationTime.month.getDisplayName(TextStyle.SHORT, Locale.US))
                    append("-")
                    append(creationTime.year)
                    append("<col>")
                })
            }
        )
        sendClientScript(5509,
            "Group Name:|<col=ffb83f>${group.name}</col>|Rename",
            interfaceScript.id component 11,
            1,
            -1
        )
        if (group.isLeader(this@updateGimSettings))
            sendClientScript(
                5509,
                "Ownership:|<col=ffb83f>Click here</col> to <col=ff0000>resign</col> as group leader.|Resignation",
                interfaceScript.id component 12,
                1,
                -1
            )
        sendClientScript(
            5509,
            "Group Members:|${group.formatMemberNames()}",
            interfaceScript.id component 13,
            0,
            0
        )
    }

    fun StringBuilder.appendColouredLine(text: String) =
        append("<col=ffb83f>$text</col><br>")

    fun IronmanGroup.formatMemberNames() =
        allMembers.joinToString(", ") { TextUtils.formatName(it.username) }

}