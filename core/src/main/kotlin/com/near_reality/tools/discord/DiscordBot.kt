package com.near_reality.tools.discord

import com.zenyte.game.world.entity.player.Player
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.createChatInputCommand
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.channel.NewsChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

/**
 * Represents a discord bot.
 *
 * @author Stan van der BEnd
 */
open class DiscordBot {

    val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * A [Kord] instance, used for interacting with Discord's API.
     */
    lateinit var kord: Kord

    /**
     * The [DiscordServer] that his bot is initialised for.
     */
    lateinit var server: DiscordServer

    /**
     * Get the [Guild] instance for this [server].
     */
    val guild: Guild
        get() = getGuild(server.guildId)

    /**
     * Get the general chat [TextChannel] for this [guild].
     */
    val generalChannel: TextChannel
        get() = getChannel(server.generalChannelId)

    val automatedDetectionChannel: TextChannel?
        get() =
            if(server is DiscordServer.Staff)
                getChannel(DiscordServer.Staff.automatedDetectionChannelId)
            else null


    /**
     * Get the broadcast [TextChannel] for this [guild].
     */
    val broadcastChannel: NewsChannel
        get() = getChannel(server.broadcastChannelId)

    private val commands = ConcurrentHashMap<String, suspend ChatInputCommandInteraction.() -> Unit>()

    fun init(server: DiscordServer, token: String) {
        this.server = server
        runBlocking {
            kord = Kord(token)
            onInit(kord)
            kord.on<ChatInputCommandInteractionCreateEvent> {
                val command = interaction.command
                try {
                    val commandHandler = commands[command.rootName]
                    commandHandler?.invoke(interaction)
                } catch (e: Exception) {
                    logger.error("Failed to handle command {}", command, e)
                }
            }
            logger.debug("Initialised Kord")
        }
        thread(start = true, name = this::class.simpleName ?: "discord-bot") {
            runBlocking {
                kord.login {
                    presence {
                        watching("Near Reality")
                    }
                }
            }
        }
    }

    fun initStaff(token: String) {
        this.server = DiscordServer.Staff
        runBlocking {
            kord = Kord(token)
            onInit(kord)
            kord.on<ChatInputCommandInteractionCreateEvent> {
                val command = interaction.command
                try {
                    val commandHandler = commands[command.rootName]
                    commandHandler?.invoke(interaction)
                } catch (e: Exception) {
                    logger.error("Failed to handle command {}", command, e)
                }
            }
            logger.debug("Initialised Kord")
        }
        thread(start = true, name = this::class.simpleName ?: "discord-bot") {
            runBlocking {
                kord.login {
                    presence {
                        watching("Near Reality")
                    }
                }
            }
        }
    }

    suspend fun registerChatInputCommand(name: String, description: String,
                                 builder: ChatInputCreateBuilder.() -> Unit,
                                 handler: suspend ChatInputCommandInteraction.() -> Unit) {
        if (commands.contains(name))
            error("A command is already registered for key $name")
        guild.createChatInputCommand(name, description, builder)
        commands[name] = handler
    }

    open fun onInit(kord: Kord) {}

    inline fun <reified T : Channel> getChannel(id: Snowflake): T = runBlocking {
        if (::kord.isInitialized)
            kord.getChannelOf(id)
                ?: error("no channel found for id $id")
        else
            error("kord instance for bot is not initialised")
    }

    private fun getGuild(id: Snowflake): Guild = runBlocking {
        if (::kord.isInitialized)
            kord.getGuildOrNull(id) ?: error("no channel found for id $id")
        else
            error("kord instance for bot is not initialised")
    }

    fun isInitialized() = this::kord.isInitialized

    suspend fun sendAutoDetection(player: Player, command: String, banApplied: Boolean) {
        if (!banApplied) {
            val message = """
                **Please Review**
                
                ${player.username} (${player.ip}) - DEBUG
                ```
                Using Bot Client Now: true
                JVM Command: $command
                Encountered Exception: false
                ```
            """.trimIndent()
            automatedDetectionChannel?.createMessage(message)
        } else {
            val message = """
                **Ban Applied Automatically**
                
                ${player.username} (${player.ip})
                ```
                Using Bot Client Now: true
                JVM Command: $command
                Encountered Exception: false
                ```
            """.trimIndent()
            automatedDetectionChannel?.createMessage(message)
        }

    }
}
