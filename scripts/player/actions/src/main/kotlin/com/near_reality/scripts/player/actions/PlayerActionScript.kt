package com.near_reality.scripts.player.actions

import com.near_reality.game.world.entity.player.PlayerActionPlugin
import com.near_reality.scripts.player.PlayerScript
import com.zenyte.game.world.entity.player.Player
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Stan van der Bend
 */
@KotlinScript(
    "Player Action Script",
    fileExtension = "playeraction.kts",
    compilationConfiguration = PlayerActionCompilation::class
)
abstract class PlayerActionScript : PlayerActionPlugin(), PlayerScript {

    private var handleBody: (() -> Unit)? = null

    fun handle(body: () -> Unit) {
        handleBody = body
    }

    override fun handle() =
        handleBody?.invoke()?:Unit


    operator fun String.invoke(handle: Context.() -> Boolean) {
        bind(this) { player, npc ->
            Context(player, npc).handle()
        }
    }
}
