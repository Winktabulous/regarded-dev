package com.near_reality.tools.discord

import com.near_reality.game.world.entity.player.discordUserId
import com.zenyte.game.item._Item
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.TextUtils
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.NewsChannel
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.integer
import dev.kord.rest.builder.interaction.string
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import mgi.types.config.items.ItemDefinitions
import kotlin.reflect.KSuspendFunction1

fun itemInventoryImageUrl(itemId: Int) =
    "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/item/$itemId/public"

private val luckExpressions = listOf(
    "Such luck",
    "How fortunate",
    "Many envy you",
    "That's quite the stroke of luck",
    "Nice one",
    "Gz"
)

private val skillfulExpressions = listOf(
    "You did it",
    "Nice work",
    "Amazing",
    "That's quite a feat",
    "Well done",
    "Congratulations",
    "Nice job",
    "Good work",
    "You're a true champion",
    "Many must envy you",
    "Gz"
)

private val pitifulExpressions = listOf(
    "That's very unfortunate",
    "What a shame",
    "More luck next time",
    "Oof",
    ":(",
    "It can happen to the best of us",
    "I hope this will be the last time",
)

private val thankfulExpressions = listOf(
    "Thanks",
    "Thank you",
    "Keep it up",
    "Nice",
    "Nice one",
)

suspend fun NewsChannel.congratulatoryDescription(player: Player, baseText: String) =
    descriptive(player, baseText, ::congratulate)

suspend fun NewsChannel.thanksDescription(player: Player, baseText: String) =
    descriptive(player, baseText, ::thanks)

suspend fun NewsChannel.luckyDescription(player: Player, baseText: String) =
    descriptive(player, baseText, ::lucky)

suspend fun NewsChannel.pityDescription(player: Player, baseText: String) =
    descriptive(player, baseText, ::pity)

suspend fun descriptive(player: Player, baseText: String, provider: KSuspendFunction1<Player, String?>) =
    buildString {
        append(baseText)
        append(provider(player))
    }

suspend fun NewsChannel.congratulate(player: Player) = prefixPlayer(player, skillfulExpressions)

suspend fun NewsChannel.thanks(player: Player) = prefixPlayer(player, thankfulExpressions)

suspend fun NewsChannel.lucky(player: Player) = prefixPlayer(player, luckExpressions)

suspend fun NewsChannel.pity(player: Player) = prefixPlayer(player, pitifulExpressions)

private suspend fun NewsChannel.prefixPlayer(player: Player, strings: List<String>) = try {
    player.discordUserId?.let {
        guild.getMember(Snowflake(it.toULong()))
    }?.run {
        "\n${strings.random()} $mention!"
    }?:""
} catch (e: Exception) {
    World.log.error("Failed to get discord user for {} with id {}", player.name, player.discordUserId, e)
    ""
}

fun formatAmountText(var0: Long, pad: Int = 4): String {
    return when {
        var0 < 1_000 -> "$var0"
        var0 < 1_000_000 -> "${"${var0 / 1000}".padEnd(pad)} K"
        var0 < 1_000_000_000 -> "${"${var0 / 1_000_000}".padEnd(pad)} Million"
        else -> "${"${var0 / 1_000_000_000}".padEnd(pad)} Billion"
    }
}

fun Int2ObjectLinkedOpenHashMap<out _Item>.calculateValue() =
    values.sumOf { it.amount * ItemDefinitions.getSellPrice(it.id).toLong() }

fun ChatInputCreateBuilder.includeUsernameOption() {
    string("username", "The name of the user you'd like to perform a search for") {
        required = true
        maxLength = 13
        minLength = 1
    }
}

fun ChatInputCreateBuilder.includeDaysAgoOption() {
    integer("days_ago", "The number of days before now to retrieve logs from (default = 1)") {

        required = false
    }
}

fun ChatInputCommandInteraction.readUsername(): String =
    requireNotNull(TextUtils.formatNameForProtocol(command.strings["username"]!!)) {
        "Missing `username` field in command $command"
    }

fun ChatInputCommandInteraction.readDaysAgo(): Int =
    command.integers["days_ago"]?.toInt()?.coerceIn(0..25)?:0
