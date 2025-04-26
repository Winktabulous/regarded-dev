package com.near_reality.tools.discord.community

import com.near_reality.game.util.invoke
import com.near_reality.game.world.entity.player.disableDiscordLinkRequests
import com.near_reality.game.world.entity.player.discordUserId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour.MAROON
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import dev.kord.core.Kord
import dev.kord.core.behavior.createChatInputCommand
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.EphemeralMessageInteractionResponseBehavior
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

private val requestTimeMap = ConcurrentHashMap<ULong, Instant>()

/**
 * Handles the linking a [Player] to a discord account.
 *
 * @author Stan van der Bend
 */
fun DiscordCommunityBot.configureAccountLinking(kord: Kord) {
    scope.launch {
        guild.createChatInputCommand("link", "Link your in-game account to your discord account") {
            string("username", "The name of your in-game account") {
                required = true
            }
        }
    }
    kord.on<ChatInputCommandInteractionCreateEvent> {

        val discordUsername = interaction.user.username
        val discordUserId = interaction.user.id.value

        val currentTime = Clock.System.now()
        val lastRequest = requestTimeMap[discordUserId]
        if (lastRequest != null && (currentTime - lastRequest < 10.seconds)) {
            interaction.respondEphemeral {
                content =
                    "You can only send a link request every 10 seconds, please wait a while before trying again."
            }
            return@on
        }

        val command = interaction.command
        val username = command.strings["username"]!!.trim().lowercase()

        val player = findPlayerByUsername(username)
        if (player == null)
            respond(interaction, "Did not find an online account by the name of **$username**")
        else {
            if (player.disableDiscordLinkRequests) {
                respond(
                    interaction,
                    "The player by the name of **$username** has Disabled discord link requests." +
                            "If this is you, you can enable it by typing `::enablediscordlinking` in-game."
                )
                return@on
            }
            val response =  respond(interaction, "You can finish the linking in-game.")
            WorldTasksManager.schedule {
                showLinkRequestDialogue(player, discordUsername, response, username, discordUserId)
            }
        }
    }
}

private fun DiscordCommunityBot.showLinkRequestDialogue(
    player: Player,
    discordUsername: String,
    interaction: EphemeralMessageInteractionResponseBehavior,
    username: String,
    discordUserId: ULong,
) {
    player.dialogue {
        plain(
            "You received a request to link your player account to the Discord account ${MAROON(discordUsername)}. " +
                    "When linked, the discord user can request information about your account. " +
                    "Please make sure this is you, do not link other people their discord to your account!"
        )
        options("Are you sure you wish to link Discord account ${MAROON(discordUsername)}?") {
            "Yes." {
                edit(
                    interaction,
                    "You successfully linked your Discord account to player **${username}**"
                )
                player.discordUserId = discordUserId.toLong()
                player.dialogue {
                    plain(
                        "You successfully linked the Discord account ${MAROON(discordUsername)}." +
                                "Would you like to disable future discord link requests?"
                    )
                    options("Disabled future discord link requests?") {
                        "Yes." { disableDiscordLinking(player) }
                        "No." {}
                    }
                }
            }
            "No." {
                edit(
                    interaction,
                    "The request to link you to the player **$username** was denied by the player"
                )
                player.options("Would you link to disable discord linkage requests?") {
                    "Yes." { disableDiscordLinking(player) }
                    "No." {}
                }
            }
        }
    }
}

private suspend fun respond(interaction: ChatInputCommandInteraction, content: String) = interaction.respondEphemeral {
    this.content = content
}

private fun DiscordCommunityBot.edit(interaction: EphemeralMessageInteractionResponseBehavior, content: String) = scope.launch {
    interaction.edit {
        this.content = content
    }
}
private fun findPlayerByUsername(username: String) = World.getPlayers().find {
    it.name.equals(username, true) || it.username.equals(username, true)
}

private fun disableDiscordLinking(player: Player) {
    player.disableDiscordLinkRequests = true
    player.sendMessage("Disabled Discord link request, enable it by executing ::enablediscordlinking")
}
