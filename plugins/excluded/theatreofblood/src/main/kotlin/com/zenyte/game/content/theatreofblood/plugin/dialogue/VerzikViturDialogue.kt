package com.zenyte.game.content.theatreofblood.plugin.dialogue

import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue

/**
 * @author Jire
 */
class VerzikViturDialogue(player: Player, npcID: Int) : Dialogue(player, npcID) {

    override fun buildDialogue() {
        val area = VerSinhazaArea.getArea(player) as? VerzikViturRoom ?: return
        if (area.started) return
        if (!area.raid.party.isLeader(player)) {
            npc("Your leader ${area.raid.party.leader!!.name} must speak to me, not you!")
            return
        }

        npc("Now that was quite the show!<br>I haven't been that entertained in a long time.")
        npc("Of course, you know I can't let you leave here alive.<br>Time for your final performance...")
        options(
            "Is your party ready to fight?",
            DialogueOption("Yes, let's begin.", key(100)),
            DialogueOption("No, don't start yet.", key(200))
        )

        player(100, "Yes, let's begin.")
        npc("Oh I'm going to enjoy this...")
            .executeAction { area.start(player) }

        player(200, "No, don't start yet.")
    }

}