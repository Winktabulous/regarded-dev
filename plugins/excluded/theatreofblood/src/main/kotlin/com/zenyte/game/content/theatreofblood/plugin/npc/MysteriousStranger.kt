package com.zenyte.game.content.theatreofblood.plugin.npc

import com.zenyte.game.content.theatreofblood.plugin.dialogue.MysteriousStrangerDialogue
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.VarManager

/**
 * @author Jire
 * @author Tommeh
 */
class MysteriousStranger : NPCPlugin() {

    override fun handle() {
        bind("Talk-to", object : OptionHandler {
            override fun handle(player: Player, npc: NPC) = startInitialDialogue(player, npc.id)

            override fun execute(player: Player, npc: NPC) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
        bind("Trade", object : OptionHandler {
            override fun handle(player: Player, npc: NPC) = player.openShop("Mysterious Stranger")

            override fun execute(player: Player, npc: NPC) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
    }

    override fun getNPCs() = npcs

    internal companion object {

        const val DIALOGUE_VARBIT = 12973

        fun completedInitialDialogue(player: Player) =
            player.varManager.getBitValue(DIALOGUE_VARBIT) == 1

        private val npcs = intArrayOf(NpcId.MYSTERIOUS_STRANGER, 10875, 10876) // TODO

        fun startInitialDialogue(player: Player, npcID: Int = npcs[0]) =
            player.dialogueManager.start(MysteriousStrangerDialogue(player, npcID))

        init {
            VarManager.appendPersistentVarbit(DIALOGUE_VARBIT)
        }

    }

}