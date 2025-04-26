package com.near_reality.tools.discord.staff.log_search

import com.near_reality.tools.discord.calculateValue
import com.near_reality.tools.discord.formatAmountText
import com.near_reality.tools.discord.includeDaysAgoOption
import com.near_reality.tools.discord.includeUsernameOption
import com.near_reality.tools.discord.readDaysAgo
import com.near_reality.tools.discord.readUsername
import com.near_reality.tools.discord.staff.DiscordStaffBot
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.file.nrJson
import com.near_reality.tools.logging.file.readGameLogsSince
import com.zenyte.game.world.entity.player.PlayerLogger
import com.zenyte.utils.TextUtils
import dev.kord.common.Color
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.time.LocalDate
import kotlin.time.Duration.Companion.days

fun DiscordStaffBot.configureLogSearchCommand() {
    launchGated {
        registerChatInputCommand("search_player_trades", "Search game logs",
            builder = {
                includeUsernameOption()
                includeDaysAgoOption()
            },
            handler = {
                scope.launch {
                    val username = readUsername()
                    val daysBack = readDaysAgo()
                    val tradeMessages = readGameLogsSince<GameLogMessage.Trade>(daysBack.days)

                    var totalTradedValue = 0L
                    val embeds = mutableListOf<EmbedBuilder>()
                    for ((day, trades) in tradeMessages) {
                        val tradesByUsername = trades.filter { it.username == username }
                        if (tradesByUsername.isNotEmpty()) {
                            val usersTradedWith = tradesByUsername.map { it.otherUsername }.toSet()
                            val totalValueTradedToday =
                                tradesByUsername.sumOf { it.items.calculateValue() + it.otherItems.calculateValue() }
                            embeds += EmbedBuilder().apply {
                                title = "Trades at $day for $username"
                                field("People", inline = true) { usersTradedWith.joinToString() }
                                field("Value", inline = true) { formatAmountText(totalValueTradedToday) }
                            }
                            totalTradedValue += totalValueTradedToday
                        }
                    }
                    respondPublic {
                        content = "Showing trades from the last **$daysBack** for **$username**"
                        val ourEmbeds = this.embeds
                        if (ourEmbeds != null) {
                            ourEmbeds += embeds
                        }
                        val bytes = nrJson.encodeToString(tradeMessages).toByteArray()
                        addFile("trades_$username.json", ChannelProvider(bytes.size.toLong()) {
                            bytes.inputStream().toByteReadChannel()
                        })
                    }
                }
            }
        )
        registerChatInputCommand("download_player_logs", "Search game logs for all players",
            builder = {
                string("username", "The name of the user you'd like to download logs of")
            },
            handler = {
                scope.launch {
                    val username = TextUtils.formatNameForProtocol(command.strings["username"]!!)
                    val daysBack = command.integers["days_ago"]?.toInt()?.coerceIn(0..25)?:0
                    val logsPath = PlayerLogger.getPath(LocalDate.now().minusDays(daysBack.toLong()))
                    val playerLogsFile = logsPath.resolve("$username.log")
                    respondPublic {
                        if (Files.exists(playerLogsFile)) {
                            val logs = withContext(Dispatchers.IO) {
                                Files.readString(playerLogsFile)
                            }
                            val logsBytes = logs.toByteArray()
                            content = "Including logs from `$logsPath` for user $username"
                            addFile(logsPath.toString(), ChannelProvider(logsBytes.size.toLong()) {
                                logsBytes.inputStream().toByteReadChannel()
                            })
                        } else {
                            embed {
                                title = "Logs not found"
                                description = "Did not find logs for **$username** at `$logsPath`"
                                color = Color(226, 18, 18)
                            }
                        }
                    }
                }
            }
        )
    }
}


