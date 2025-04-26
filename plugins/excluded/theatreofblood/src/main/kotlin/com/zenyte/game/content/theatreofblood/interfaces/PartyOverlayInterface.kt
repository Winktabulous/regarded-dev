package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player

/**
 * @author Tommeh
 * @author Jire
 */
class PartyOverlayInterface : Interface() {

    override fun attach() {
        put(12, "Party members")
        put(15, "Exit Spectating")
    }

    override fun build() {
        bind("Exit Spectating") { player: Player ->
            VerSinhazaArea.getParty(
                player,
                checkMembers = false,
                checkViewers = false,
                checkSpectators = true
            )?.raid?.spectators?.remove(player.username)

            fadeRedPortal(player, "")
            WorldTasksManager.schedule {
                fade(player, 200, 0, "")
                player.setLocation(TheatreOfBloodRaid.outsideLocation)
            }
        }
    }

    override fun getInterface() = GameInterface.TOB_PARTY

    internal companion object {

        fun refresh(player: Player, party: RaidingParty?) {
            val optionalPlugin = GameInterface.TOB_PARTY.plugin
            if (!optionalPlugin.isPresent) return
            val plugin = optionalPlugin.get()

            plugin.open(player)
            val builder = StringBuilder()
            for (index in 0..4) {
                if (party == null || index >= party.members.size) {
                    builder.append("-<br>")
                    continue
                }
                val member = party.getMember(index) ?: continue
                builder.append(member.name + "<br>")
            }
            player.varManager.sendBit(6440, if (party == null) 0 else 1)
            player.packetDispatcher.sendComponentText(
                plugin.getInterface(),
                plugin.getComponent("Party members"),
                builder.toString()
            )
        }

        fun fade(player: Player, trans: Int, instant: Int, text: String) =
            player.packetDispatcher.sendClientScript(2306, trans, instant, text)

        fun fadeRedPortal(player: Player, text: String) = player.packetDispatcher.sendClientScript(2307, text)

        fun fadeWhite(player: Player, text: String) = player.packetDispatcher.sendClientScript(2308, text)

    }

}