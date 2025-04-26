package com.zenyte.game.world.entity.player.dialogue

import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
class OptionsBuilder(val dialogue: Dialogue, val title: String) {

    val options: MutableList<Dialogue.DialogueOption> = ArrayList(MAX_OPTIONS)

    @JvmName("option")
    operator fun String.invoke(runnable: Runnable? = null): Boolean {
        if (options.size >= MAX_OPTIONS)
            throw IllegalStateException("You can't have more than $MAX_OPTIONS options")

        val dialogueOption = Dialogue.DialogueOption(this, runnable)
        return options.add(dialogueOption)
    }

    operator fun Dialogue.DialogueOption.invoke(runnable: Runnable? = null) {
        this.runnable = runnable
        options.add(this)
    }

    @JvmName("option")
    operator fun String.invoke(key: Int) = invoke(dialogue.key(key))

    fun dialogueOption(
        text: String,
        noPlayerMessage: Boolean = false,
        action: (Player) -> Unit = {},
        buildDialogue: Dialogue.() -> Unit = {},
    ) = text.invoke {
        val player = dialogue.player
        requireNotNull(player)
        player.dialogue(dialogue.npcId) {
            if (!noPlayerMessage)
                player(text).executeAction { action(player) }
            else
                action(player)
            buildDialogue(this)
        }
    }

    fun addOptionMessage(): OptionMessage = dialogue.options(title, *options.toTypedArray())

    companion object {
        private const val MAX_OPTIONS = 5
    }

}
