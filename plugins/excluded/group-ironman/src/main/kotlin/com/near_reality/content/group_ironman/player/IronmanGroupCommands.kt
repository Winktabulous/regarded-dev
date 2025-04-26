@file:Suppress("unused")

package com.near_reality.content.group_ironman.player

import com.google.common.eventbus.Subscribe
import com.near_reality.game.packet.out.chat_channel.ChatChannelVar
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.events.ServerLaunchEvent

object IronmanGroupCommands {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent?) {
        register()
    }

    private fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "message") {p, args ->
            p.sendMessage("bla | bla", MessageType.CLAN_GIM_FORM_GROUP, p.username)
        }
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "varclan") { p, args ->
            val group = p.finalisedIronmanGroup
            if (group == null) {
                p.sendMessage("No group.")
                return@Command
            }

            val v = ChatChannelVar.find(args[0].toInt())
            if (v == null) {
                p.sendMessage("Invalid varclan id entered.")
                return@Command
            }

            group.channel.setVariableInt(v, args[1].toInt())
        }
    }
}
