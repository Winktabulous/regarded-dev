package com.zenyte.game.content.theatreofblood

import com.google.common.eventbus.Subscribe
import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.plugins.events.PostWindowStatusEvent

/**
 * @author Jire
 * @author Tommeh
 */
object TheatreOfBloodLoginSubscriber {

    internal const val LAST_TOB_PARTY_ID = "last_tob_party_id"

    private val loginLocation = Location(3372, 5155, 2)

    @Subscribe
    @JvmStatic
    fun onLogin(event: PostWindowStatusEvent) {
        val player = event.player
        if (!player.attributes.containsKey(LAST_TOB_PARTY_ID)) return

        val id = player.getNumericAttribute(LAST_TOB_PARTY_ID).toInt()
        player.attributes.remove(LAST_TOB_PARTY_ID)

        player.setLocation(loginLocation)

        GameInterface.TOB_PARTY.open(player)
        PartyOverlayInterface.fade(player, 0, 1, "Seeking party...")

        WorldTasksManager.schedule({
            val party = VerSinhazaArea.getParty(id)
            val raid = party?.raid
            if (raid == null || raid.completed) {
                player.setLocation(TheatreOfBloodRaid.outsideLocation)
                PartyOverlayInterface.fade(player, 200, 0, "Unable to rejoin party.")
            } else for (m in party.originalMembers) {
                val p = World.getPlayer(m)
                if (p.isPresent && p.get().username == player.username) {
                    raid.onLogin(player)
                    break
                }
            }
        }, 2)
    }

}