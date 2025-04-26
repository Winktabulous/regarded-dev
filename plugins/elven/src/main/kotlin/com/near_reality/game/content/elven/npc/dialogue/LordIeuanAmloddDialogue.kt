package com.near_reality.game.content.elven.npc.dialogue

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Elgan.
 */
class LordIeuanAmloddDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        npc("Good day ${player.name}. Have you come to hear the song of the elves?")
        player("What do you mean?")
        npc("Do you not hear it? The sounds of a city in harmony. It is the song of the elves, the story of our people.")
        npc("The last few verses were dark and quiet, but now our city is restored, we find ourselves singing a new verse, one of joy and light.")
        player("I see. I guess I've never been the type to reflect on that sort of thing.")
        npc("More of a fighter? We have a song for that too. If you wish to sing it, head to the Gauntlet and put yourself to the test.")
        player("I might just give it a shot. Thanks.")
    }
}
