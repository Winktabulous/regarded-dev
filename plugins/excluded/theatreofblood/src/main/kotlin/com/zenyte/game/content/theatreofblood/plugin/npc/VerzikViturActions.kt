package com.zenyte.game.content.theatreofblood.plugin.npc

import com.zenyte.game.content.theatreofblood.plugin.dialogue.VerzikViturDialogue
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturPhase
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
class VerzikViturActions : NPCPlugin() {
    
    override fun handle() {
        bind("Talk-to", object : OptionHandler {
            override fun handle(player: Player, npc: NPC) =
                player.dialogueManager.start(VerzikViturDialogue(player, npc.id))

            override fun execute(player: Player, npc: NPC) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
    }

    override fun getNPCs() = intArrayOf(VerzikViturPhase.NONE.npcID)

}