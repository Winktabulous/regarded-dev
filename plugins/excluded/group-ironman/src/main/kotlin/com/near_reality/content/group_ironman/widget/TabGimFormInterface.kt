package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.IronmanGroupModule
import com.near_reality.content.group_ironman.player.inIronmanGroupCreationInterface
import com.near_reality.content.group_ironman.player.pendingIronmanGroup
import com.near_reality.game.model.ui.chat_channel.ChatChannelType
import com.near_reality.game.model.ui.chat_channel.chatChannelInterfaceType
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.util.Colour
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.loadingDialogueOnWorldThread
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.utils.TextUtils
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*

class TabGimFormInterface : InterfaceScript() {
    init {
        FORM_GIM_TAB {
            "Refresh"(1) {
                player.updateGimMembers()
            }
            "Group name"(8) {
                val group = player.pendingIronmanGroup!!
                if (group.leaderUsername != player.username) {
                    player.packetDispatcher.sendClientScript(5304)
                } else {
                    player.sendInputString("Propose a group name: (up to 12 characters)") { name ->
                        checkGroupNameAvailability(player, name, group) {
                            group.name = name
                        }
                    }
                }
            }
            "Invite"(9) {
                player.sendMessage("You're now in Invite Target Mode. Left click someone to invite them.")
                player.sendMessage("You're no longer in Invite Target Mode.")
            }
            "Cancel"(10) {
                player.options("Really leave? This will cancel the creation of the group.") {
                    "Yes" {
                        player.pendingIronmanGroup!!.allMembers.forEach {
                            it.ifOnline {
                                sendMessage("You are no longer creating a group.")
                                pendingIronmanGroup = null
                                ChatChannelType.IronGroup.gameInterface.open(this)
                            }
                        }
                    }
                    "No" {}
                }
            }
            "Confirm"(11) {
                val group = player.pendingIronmanGroup!!
                if (group.leaderUsername != player.username) {
                    player.packetDispatcher.sendClientScript(5304)
                } else {
                    if (group.allMembers.size < 2)
                        player.sendMessage("You need at least 2 people before you can create the group.")
                    else {
                        fun tryForm(name: String) {
                            player.sendMessage("Checking pending name...")
                            checkGroupNameAvailability(player, name, group) {
                                group.name = name
                                player.dialogue {
                                    plain(
                                        "You are about to create the group ${Colour.BLUE.wrap(group.name)}. Once finalised, you cannot " +
                                                "change group members for a while, and inviting more members will " +
                                                "cause you to lose your hardcore and prestige status."
                                    )
                                    options("Create group ${Colour.BLUE.wrap(group.name)}") {
                                        "Yes" {
                                            IronmanGroup.finaliseCreation(group)
                                        }
                                        "No" {}
                                    }
                                }
                            }
                        }
                        if (!group.hasName())
                            player.sendInputString("What would you like to name your group?", ::tryForm)
                        else
                            tryForm(group.name)
                    }
                }
            }
            opened {
                val group = pendingIronmanGroup
                if (group == null) {
                    sendDeveloperMessage("Not opening interface because you're not in a pending group.")
                    return@opened
                }
                inIronmanGroupCreationInterface = true
                if (group.leaderUsername == username) {
                    varManager.sendBitInstant(13058, 1)
                    varManager.sendBitInstant(13059, 1)
                } else {
                    varManager.sendBitInstant(13058, 0)
                    varManager.sendBitInstant(13059, 1)
                }
                chatChannelInterfaceType.sendTabInterface(this, getInterface())
                updateGimMembers()
            }
            closed {
                inIronmanGroupCreationInterface = false
            }
        }
    }

    fun Player.updateGimName(name: String) =
        packetDispatcher.sendClientScript(4447, id component 0, id component 8, name, 4053)

    fun Player.updateGimMembers() = packetDispatcher.run {
        val group = pendingIronmanGroup?:return@run
        sendClientScript(915, 7)
        sendClientScript(4448, id component 7, *group.getMemberNames(), group.leaderUsername)
    }

    fun IronmanGroup.getMemberNames(): Array<String> {
        val array = Array(5) { "" }
        for ((i, m) in allMembers.withIndex()) {
            array[i] = TextUtils.formatName(m.username)
        }
        return array
    }

    fun checkGroupNameAvailability(
        player: Player,
        name: String,
        group: IronmanGroup,
        onAvailable: () -> Unit = {},
    ) {
        player.loadingDialogueOnWorldThread("Checking availability...") {
            val nameAvailable = !IronmanGroupModule.checkIfNameNotAvailable(name)
            val coloredName = (if (nameAvailable) {
                player.sendMessage("Group name is available. Keep in mind that it is not reserved.")
                Colour.GREEN
            } else {
                player.sendMessage("The group name is taken.")
                Colour.RED
            }).wrap(name)

            player.updateGimName(coloredName)
            if (nameAvailable) {
                onAvailable()
                group.allMembers.forEach {
                    it.ifOnline {
                        if (this != player) {
                            updateGimName(Colour.GREY.wrap(name))
                        }
                    }
                }
            }
        }
    }
}
