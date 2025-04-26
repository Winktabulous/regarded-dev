package com.near_reality.game.plugin

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.dialogue.MakeType
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.zenyte.plugins.dialogue.SkillDialogue

inline fun<reified T> Player.optionsMenu(
    options: List<T>,
    title: String = "Select a ${T::class.simpleName}",
    cancellable: Boolean = true,
    crossinline stringifier: (T) -> String = { it.toString() },
    crossinline action: (T) -> Unit
) = dialogueManager.start(object : OptionsMenuD(
    this,
    title,
    *options.map(stringifier).toTypedArray()
) {
    override fun handleClick(slotId: Int) {
        val option = options.getOrNull(slotId) ?: return
        action(option)
    }
    override fun cancelOption(): Boolean =
        cancellable
})

fun Player.skillOptions(
    options: List<Item>,
    title: String = SkillDialogue.TITLE,
    type: MakeType = MakeType.MAKE,
    maxAmount: Int = 28,
    action: SkillOptionContext.() -> Unit
) = dialogueManager.start(object : SkillDialogue(
    this,
    title,
    *options.toTypedArray()
) {
    override fun run(slotId: Int, amount: Int) {
        val item = options.getOrNull(slotId) ?: return
        action(SkillOptionContext(slotId, amount, item))
    }

    override fun getMaximumAmount(): Int {
        return maxAmount
    }
    override fun type(): MakeType {
        return type
    }
})

data class SkillOptionContext(val slotId: Int, val amount: Int, val item: Item)
