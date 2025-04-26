package com.zenyte.game.content.theatreofblood.plugin.dialogue

import com.zenyte.game.content.theatreofblood.plugin.npc.MysteriousStranger
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue

/**
 * @author Jire
 * @author Tommeh
 */
class MysteriousStrangerDialogue(player: Player, npcID: Int) : Dialogue(player, npcID) {

    override fun buildDialogue() {
        if (MysteriousStranger.completedInitialDialogue(player))
            buildRegularDialogue()
        else buildInitialDialogue()
    }

    private fun buildInitialDialogue() {
        npc("You look like someone who can handle themselves. Are you intending to partake in the Theatre?")
        player("Maybe?")
        npc("You're wondering what's in it for you I suppose. The citizens of Meiyerditch come here looking for freedom, but I can tell you're no citizen.")
        npc("Don't worry, you're not the first to sneak in. The vampyres here turn a blind eye to that sort of thing. Outsiders generally perform better in the Theatre so make for more interesting entertainment.")
        npc("Even the outsiders don't survive though, no one does. You, however, might just have what it takes.")
        player("Where are you going with this?")
        npc("You're strong enough to survive the Theatre but you need a reason to try. I have a little proposition for you.")
        player("Go on...")
        npc("The Theatre is owned by Lady Verzik Vitur. I represent a party that has certain... interests in Verzik.")
        npc("Enter the Theatre and beat the challenges within. Doing so would cause be quite the embarrassment for Verzik, something that my associates would very much appreciate.")
        player("So you just need me to embarrass her? Doesn't sound too bad.")
        npc("Be aware, this will be no easy challenge. I doubt you'll succeed alone. However, the vampyres will let you enter in a group up to five. I suggest you take advantage of this.")
        npc("You can use the notice board to find suitable allies with whom to enter the Theatre.")
            .executeAction {
                player.varManager.sendBit(MysteriousStranger.DIALOGUE_VARBIT, 1)
            }
    }

    private fun buildRegularDialogue() {
        npc("Any luck in the Theatre?")
        options(
            TITLE,
            DialogueOption("What am I meant to be doing again?", key(100)),
            DialogueOption("I've managed to defeat her!", key(200))
        )

        player(100, "What am I meant to be doing again?")
        npc("Enter the Theatre and beat the challenges within. Doing so would cause great embarrassment to Verzik and my associates would very much appreciate it.")

        player(200, "I've managed to defeat her! I survived every single one of Verzik's challenges!")
        if (player.getNumericAttribute("theatreofblood").toInt() > 0) { // TODO refactor into method
            player("I've managed to defeat her! I survived every single one of Verzik's challenges!")
            player("At the end, she even chose to face me herself. I couldn't fully defeat her though, she just transformed into a bat and flew away.")
            npc("Impressive. Don't stop there though, return to the Theatre and embarrass her again. Keep it up and you will be rewarded.")
        } else {
            npc("Really?")
            player("No.")
            npc("I suggest you don't waste my time.")
        }
    }

}