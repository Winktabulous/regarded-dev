package com.near_reality.tools.discord.staff.eco_search

import com.near_reality.tools.WealthScanner
import com.near_reality.tools.discord.DiscordServer
import com.near_reality.tools.discord.calculateValue
import com.near_reality.tools.discord.formatAmountText
import com.near_reality.tools.discord.itemInventoryImageUrl
import com.near_reality.tools.discord.staff.DiscordStaffBot
import com.zenyte.game.world.entity.player.container.Container
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.followup.edit
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.response.createPublicFollowup
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.ActionInteraction
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mgi.types.config.items.ItemDefinitions

private const val inputId = "search_eco_text"
private const val thresholdId = "search_eco_threshold"

private var isSearching by atomic(false)
private var searchType by atomic<EcoSearchType?>(null)

private const val modelId = "eco_search_modal"

/**
 * Adds a `/search_eco` command to this [DiscordStaffBot.guild],
 * useful for staff members to perform economy searchers.
 *
 * @author Stan van der Bend
 */
fun DiscordStaffBot.configureEcoSearchCommand(kord: Kord) {
    launchGated {
        registerChatInputCommand("search_eco", "Perform one of various economy searches",
            builder = {
                string("type", "The type of economy search to perform") {
                    for (searchType in EcoSearchType.all)
                        choice(searchType.discordLabel, searchType.discordId)
                    required = true
                }
                boolean("cache", "Use cached player files (faster) or reload all") {
                    required = false
                }
            },
            handler = {
                if (channelId != DiscordServer.Staff.economySearchChannelId)
                    return@registerChatInputCommand

                if (!isManager(user)) {
                    respondPublic { content = "You do not have sufficient permissions to do an economy search." }
                    return@registerChatInputCommand
                }

                if (isSearching) {
                    respondPublic { content = "Someone else is currently searching, please try again later." }
                    return@registerChatInputCommand
                }
                val type = EcoSearchType.forId(command.strings["type"]!!)!!

                searchType = type
                if (command.booleans["cache"] == false)
                    cachedPlayerAccounts = null

                if (type == EcoSearchType.Value) {
                    doSearch(type, interaction = this)
                } else {
                    modal("Search economy", modelId) {
                        if (type == EcoSearchType.Item) {
                            createItemInputField(type, 0)
                            createItemInputField(type, 1)
                            createItemInputField(type, 2)
                            createItemInputField(type, 3)
                            actionRow {
                                textInput(TextInputStyle.Short, thresholdId, "threshold") {
                                    required = false
                                    allowedLength = 1..10
                                    placeholder = "Enter the minimum amount a player must have to be included in the search (default=1)"
                                }
                            }
                        }
                    }
                }
            }
        )
    }
    kord.on<ModalSubmitInteractionCreateEvent> {

        if (interaction.modalId != modelId)
            return@on

        if (isSearching) {
            interaction.respondPublic { content = "Someone else is currently searching, please try again later." }
            return@on
        }

        val searchInputs = interaction.textInputs
            .filterKeys { it.startsWith(inputId) }
            .map { it.value.value }
            .filterNotNull()
            .filterNot { it.isBlank() }

        if (searchInputs.isEmpty()) {
            interaction.respondPublic { content = "Did not find any inputs, please provide at least one." }
            return@on
        }

        val threshold = interaction.textInputs[thresholdId]?.value?.toIntOrNull()?:1
        doSearch(searchType!!, searchInputs, threshold, interaction)
    }
}

private fun ModalBuilder.createItemInputField(type: EcoSearchType, index: Int) {
    actionRow {
        textInput(TextInputStyle.Short, "${inputId}_$index", "Item #${index+1}") {
            required = index == 0
            allowedLength = 1..40
            placeholder = type.discordTextInputPlaceHolder
        }
    }
}

private fun DiscordStaffBot.doSearch(
    searchType: EcoSearchType,
    searchInputs: List<String>? = null,
    searchThreshold: Int = 1,
    interaction: ActionInteraction,
) {
    scope.launch {
        isSearching = true
        coroutineScope {
            try {
                when (searchType) {
                    EcoSearchType.Item -> searchItem(searchInputs!!, interaction, searchType, searchThreshold)
                    EcoSearchType.Player -> TODO()
                    EcoSearchType.Value -> searchValue(interaction)
                }
            } catch (e: Exception) {
                logger.error("Failed to search {} with input {}", searchType, searchInputs, e)
            }
        }
        isSearching = false
    }
}

private suspend fun DiscordStaffBot.searchValue(interaction: ActionInteraction) {
    val response = interaction.respondPublic {
        content = "Searching through all account, calculating their total worth..."
    }
    val accounts = loadPlayersAsync()
    val accountValues = accounts
        .map {
            var totalValue = 0L
            totalValue += it.bank.container.items.calculateValue()
            totalValue += it.inventory.container.items.calculateValue()
            totalValue += it.equipment.container.items.calculateValue()
            totalValue += it.lootingBag.container.items.calculateValue()
            totalValue += it.gravestone.container.items.calculateValue()
            totalValue += it.runePouch.container.items.calculateValue()
            totalValue += it.secondaryRunePouch.container.items.calculateValue()
            it.name to totalValue
        }
        .sortedByDescending { it.second }
        .take(25)
    response.edit {
        embed {
            for ((name, value) in accountValues) {
                field(name, inline = true) { formatAmountText(value) }
            }
        }
    }
}

private suspend fun DiscordStaffBot.searchItem(
    searchInputs: List<String>,
    interaction: ActionInteraction,
    searchType: EcoSearchType,
    searchThreshold: Int
) {
    val itemDefinitionList: List<ItemDefinitions> = searchInputs
        .flatMap { text ->
            text.trim().toIntOrNull()
                ?.let { listOf(ItemDefinitions.get(it)) }
                ?: ItemDefinitions.getDefinitions().filter { it?.name?.startsWith(text, true) == true }
        }
        .filterNot { it.isNoted || it.isPlaceholder }
        .sortedByDescending { it.price }
        .take(10)

    if (itemDefinitionList.isEmpty()) {
        interaction.respondEphemeral {
            content = "Did not find any items for input **${searchInputs}**"
        }
        return
    }

    val response = interaction.respondPublic {
        content = "Searching for ${itemDefinitionList.size} items..."
        embed {
            for (item in itemDefinitionList) {
                field(item.name, inline = true) {
                    item.id.toString()
                }
            }
        }
    }

    fun Container.countOf(itemDefinitions: ItemDefinitions) =
        getAmountOf(itemDefinitions.id) + getAmountOf(itemDefinitions.notedId)

    val accounts = loadPlayersAsync()

    val searchingResponse = response.createPublicFollowup {
        content = "Searching through ${accounts.size} accounts..."
    }

    val embeds = itemDefinitionList.map { item ->
        scope.async {
            val matches = accounts.associateWith { player ->
                var count: Long = player.bank.container.countOf(item).toLong()
                count += player.inventory.container.countOf(item)
                count += player.equipment.container.countOf(item)
                count += player.lootingBag.container.countOf(item)
                count += player.gravestone.container.countOf(item)
                count += player.runePouch.container.countOf(item)
                count += player.secondaryRunePouch.container.countOf(item)
                count
            }.entries

            val embed = EmbedBuilder()
            matches
                .filter { it.value >= searchThreshold }
                .sortedByDescending { it.value }
                .take(25)
                .forEach {
                    embed.field {
                        inline = true
                        name = it.key.name
                        value = formatAmountText(it.value)
                    }
                }
            if (embed.fields.isNotEmpty()) {
                embed.title = "Top 25 holders of **${item.name}** `${item.id}` (with more than >= *$searchThreshold*)"
                embed.description = "Total in economy = **${formatAmountText(matches.sumOf { it.value })}**"
                embed.image = itemInventoryImageUrl(item.unnotedOrDefault)
                embed
            } else
                null
        }
    }.awaitAll().filterNotNull()

    searchingResponse.edit {
        content = "Showing results for search of type `${searchType.discordId}` with input **$searchInputs**"
        this.embeds = embeds.toMutableList()
    }
}


fun main() {
    WealthScanner.loadDefault()
    DiscordStaffBot.init(
        DiscordServer.Staff,
        token = "OTk4MzUxNjM5ODU1NTA5NTg2.GB7otV.kqeSjIbP_ecjc__G2o7KDeLBboxUuDDmiYb2yE"
    )

    while (true) {
        Thread.sleep(1000L)
    }
}
