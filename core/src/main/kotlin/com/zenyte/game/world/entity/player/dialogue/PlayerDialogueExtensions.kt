package com.zenyte.game.world.entity.player.dialogue

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */

fun Player.dialogue(buildDialogue: Dialogue.() -> Unit) =
    dialogueManager.start(buildDialogue)

fun Player.dialogue(npcId: Int, buildDialogue: Dialogue.() -> Unit) =
    dialogueManager.start(npcId, buildDialogue)

fun Player.dialogue(npc: NPC, buildDialogue: Dialogue.() -> Unit) =
    dialogueManager.start(npc, buildDialogue)

fun Player.dialogue(npcId: Int, npc: NPC, buildDialogue: Dialogue.() -> Unit) =
    dialogueManager.start(npcId, npc, buildDialogue)

fun Player.loadingDialogueOnWorldThread(text: String, delay: Int = 1, action: Player.() -> Unit) = dialogue {
    loading(text)
    WorldTasksManager.schedule({
        try {
            action(this@loadingDialogueOnWorldThread)
        } catch (e: Exception) {
            logger.error("Failed to execute loading action {}", text, e)
        }
        finish()
    }, delay)
}

inline fun Player.options(
    title: String = Dialogue.TITLE,
    crossinline buildOptions: OptionsBuilder.() -> Unit
) = dialogue { options(title, buildOptions) }
