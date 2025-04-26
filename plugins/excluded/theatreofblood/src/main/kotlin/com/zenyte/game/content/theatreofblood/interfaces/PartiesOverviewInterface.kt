package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player

/**
 * @author Tommeh
 * @author Jire
 */
class PartiesOverviewInterface : Interface() {

    override fun attach() {
        put(3, 0, "Refresh")
        put(3, 1, "My/Make Party")
        put(3, 2, "Friends/Clan")
        put(17, "View Party")
    }

    override fun build() {
        bind("Refresh", PartiesOverviewInterface::refresh)
        bind("My/Make Party") { player: Player ->
            val party = VerSinhazaArea.getParty(player) ?: VerSinhazaArea.createParty(player)
            party.run { player.updateInformation() }
            PartyOverlayInterface.refresh(player, party)
        }
        bind("Friends/Clan") { player: Player ->
            player.varManager.flipBit(12988)
            refresh(player)
        }
        bind("View Party") { player: Player, slotId: Int, _: Int, _: Int ->
            val party = VerSinhazaArea.getPartyByIndex(slotId)
            if (party == null) {
                refresh(player)
                return@bind
            }
            party.run { player.updateInformation() }
        }
    }

    override fun getInterface() = GameInterface.TOB_PARTIES_OVERVIEW

    internal companion object {

        fun refresh(player: Player) {
            val dispatcher = player.packetDispatcher
            player.varManager.sendVar(1740, if (VerSinhazaArea.getParty(player) != null) 0 else -1)
            GameInterface.TOB_PARTIES_OVERVIEW.open(player)
            val currentParty = VerSinhazaArea.getParty(player)
            for (index in 0..45) {
                val party = VerSinhazaArea.getPartyByIndex(index)
                if (party == null || party.raid != null) {
                    dispatcher.sendClientScript(2340, index, "")
                    continue
                }

                val members = party.players
                val stringBuilder = StringBuilder()
                for (i in 0 until 5) {
                    if (i < members.size) {
                        if (i == 0) {
                            val leaderName = party.leader!!.name
                            stringBuilder.append(if (currentParty == party) Colour.WHITE.wrap(leaderName) else leaderName)
                        } else {
                            stringBuilder.append(members[i].name)
                        }
                    }
                    stringBuilder.append('|')
                }

                val partyInformation = stringBuilder.append(party.members.size).append('|')
                        .append(party.preferredSize).append('|')
                        .append(party.preferredCombatLevel).append('|')
                        .append(if (party.hardMode) "Hard" else "Normal").append('|')
                        .append(party.age).append('|')
                        .toString()
                player.packetDispatcher.sendClientScript(2340, index, partyInformation)
            }
        }

    }

}
