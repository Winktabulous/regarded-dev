package com.near_reality.tools.discord.staff.player_search

import com.near_reality.tools.PlayerUUID
import com.near_reality.tools.discord.includeUsernameOption
import com.near_reality.tools.discord.readUsername
import com.near_reality.tools.discord.staff.DiscordStaffBot
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.message.embed

fun DiscordStaffBot.configurePlayerSearchCommand(){
    launchGated {
        registerChatInputCommand("search_related_players", "Search player that share an UUID",
            builder = {
                includeUsernameOption()
            },
            handler = {

                val username = readUsername()
                val playerAccounts = loadPlayersAsync()
                val player = playerAccounts.find { it.username == username }
                if (player == null) {
                    respondPublic { content = "Did not find player account for username **$username**" }
                    return@registerChatInputCommand
                }

                val uniqueIds = player.uuids.filter { !it.contentEquals(PlayerUUID.EMPTY) }
                if (uniqueIds.isEmpty()) {
                    respondPublic { content = "Did not find a valid UUID for the player with username **$username**" }
                    return@registerChatInputCommand
                }

                val linkedAccounts = playerAccounts
                    .filter { it.uuids.any(uniqueIds::contains) }
                    .toMutableSet()
                val allUniqueIds = linkedAccounts
                    .flatMap { it.uuids }
                    .toSet()
                val otherUniqueIds = (allUniqueIds - uniqueIds)

                linkedAccounts -= player

                respondPublic {
                    embed {
                        title = "Searched for UUIDs of the player"
                        description = "Found `${linkedAccounts.size-1}` associated with the account by name **$username**"
                        if (uniqueIds.isNotEmpty()) {
                            field("Player UUIDs") {
                                uniqueIds.joinToString()
                            }
                            if (linkedAccounts.isNotEmpty()) {
                                field("Accounts with matching UIDS") {
                                    linkedAccounts.joinToString { it.username }
                                }
                                if (otherUniqueIds.isNotEmpty()) {
                                    val accountsWithOtherUniqueIds = linkedAccounts
                                        .filter { it.uuids.any(otherUniqueIds::contains) }
                                    if (accountsWithOtherUniqueIds.isNotEmpty()) {
                                        field("Accounts with matching and other UUIDs") {
                                            accountsWithOtherUniqueIds.joinToString { it.username }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

