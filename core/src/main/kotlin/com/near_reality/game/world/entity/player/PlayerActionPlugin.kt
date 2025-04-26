package com.near_reality.game.world.entity.player

import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.Plugin
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Represents a [Plugin] for handling player menu actions.
 *
 * @author Stan van der Bend
 */
abstract class PlayerActionPlugin : Plugin {

    companion object {

        private val logger = LoggerFactory.getLogger(PlayerActionPlugin::class.java)
        private val handlerMap = mutableMapOf<String, Handler>()

        fun findHandler(option: String) = Optional.ofNullable(handlerMap[option])

        fun add(c: Class<*>) {
            try {
                val action = c.getDeclaredConstructor().newInstance() as PlayerActionPlugin
                action.handle()
            } catch (e: Exception) {
                logger.error("Failed to create instance of {}", c, e)
            }
        }
    }

    /**
     * Initialise [bindings][bind] for player menu options to [handlers][OptionHandler].
     */
    abstract fun handle()

    /**
     * Binds the argued [player menu option][option] to the [handler].
     */
    fun bind(option: String, handler: OptionHandler) {
        handlerMap[option.lowercase()] = Handler(this, handler)
    }

    fun interface OptionHandler {
        fun handle(player: Player, other: Player): Boolean
    }

    class Handler(
        val plugin: PlayerActionPlugin,
        val optionHandler: OptionHandler
    )

    class Context(val player: Player, val other: Player)
}
