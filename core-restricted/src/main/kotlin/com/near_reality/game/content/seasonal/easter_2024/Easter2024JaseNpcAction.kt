package com.near_reality.game.content.seasonal.easter_2024

import com.near_reality.game.item.CustomNpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

@Suppress("unused")
class Easter2024JaseNpcAction : NPCPlugin() {

    override fun handle() {
        bind("Talk-to") { player, npc ->
            if(Easter2024Manager.enabled) {
                player.dialogue(npc) {
                    npc("Help me defeat this bloody chicken.<br>I'll exchange your broken egg shells for mystery boxes.")
                    showOptionsDialogue()
                }
            }
        }
        bind("Trade") { player, npc ->
            if(Easter2024Manager.enabled) {
                player.openShop("Broken egg shells exchange")
            }
        }
    }

    private fun Dialogue.showOptionsDialogue() {
        options {
            dialogueOption("How do I defeat the chicken?") {
                npc("She comes in four stages:<br>1. Fire, 2. Earth, 3. Water, 4. Mixed <br>Each stage is weak to its elemental opposite!<br>The final stage is weak to all elements.")
                showOptionsDialogue()
            }
            dialogueOption("Can the chicken hit me?") {
                npc("No, she can't hit you. You can only hit her.<br>She can knock you back though, so watch your step!")
                showOptionsDialogue()
            }
            dialogueOption("What do I get for defeating the chicken?" ){
                npc("You'll receive broken egg shells for your efforts.<br>Bring them to me and I'll exchange them for mystery boxes.")
                showOptionsDialogue()
            }
            dialogueOption("Is it worthwhile for non-combat players?") {
                npc("Yes, you can empty buckets of water or sand on her, or light fires below her to deal damage.<br>Just make sure to use the correct element for each phase!<br>Except for the last one where each element works.")
                showOptionsDialogue()
            }
            dialogueOption("Nevermind")
        }
    }

    override fun getNPCs() = intArrayOf(CustomNpcId.JASE_EASTER_2024)
}
