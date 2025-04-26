package com.zenyte.game.world.entity.player.dialogue

/**
 * @author Jire
 */

inline fun Dialogue.options(title: String = Dialogue.TITLE, buildOptions: OptionsBuilder.() -> Unit): OptionMessage =
    OptionsBuilder(this, title).run {
        buildOptions()
        addOptionMessage()
    }