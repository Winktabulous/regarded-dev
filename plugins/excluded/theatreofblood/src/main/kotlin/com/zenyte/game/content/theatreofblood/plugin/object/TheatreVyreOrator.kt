package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
class TheatreVyreOrator : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val party = VerSinhazaArea.getParty(player) ?: return
        if (option == "Talk-to") {
            player.dialogueManager.start(object : Dialogue(player) {
                override fun buildDialogue() {
                    npc(NpcId.VYRE_ORATOR, "Lady Verzik Vitur lets people in here to perform, not to<br>chat.", 1)
                    options(
                        TITLE,
                        "What am I supposed to do in here?",
                        "I want to resign from the party.",
                        "Sorry, I'll get on with it."
                    )
                        .onOptionOne(key(5))
                        .onOptionTwo(key(10))
                        .onOptionThree(key(15))
                    player(5, "What am I supposed to do in here?")
                    npc(
                        NpcId.VYRE_ORATOR,
                        "Pass through the barrier and face your challenge. If<br>you survive, and your struggle entertains Verzik, she<br>will grant you freedom from the blood tithes.",
                        6
                    )
                    player("Okay.")
                    player(10, "I wan to resign from the party.").executeAction {
                        resign(
                            player,
                            party
                        )
                    }
                    player(15, "Sorry, I'll get on with it.")
                }
            })
        } else if (option == "Resign") {
            resign(player, party)
        }
    }

    override fun getObjects() = TheatreVyreOrator.objects

    private companion object {

        val objects = arrayOf(ObjectId.VYRE_ORATOR, ObjectId.VYRE_ORATOR_32757)

        fun resign(player: Player, party: RaidingParty) {
            player.dialogueManager.start(object : Dialogue(player) {
                override fun buildDialogue() {
                    options("There is no penalty for resigning now.", "Resign and leave the Theatre.", "Do not resign.")
                        .onOptionOne {
                            party.raid?.leave(player, "Nice try.", false)
                        }
                        .onOptionTwo(key(5))
                    player(5, "Actually, I'll stay in for now.")
                    npc(NpcId.VYRE_ORATOR, "As you wish.", 6)
                }
            })
        }

    }

}