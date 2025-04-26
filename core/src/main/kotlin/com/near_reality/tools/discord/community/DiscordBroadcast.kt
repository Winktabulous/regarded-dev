package com.near_reality.tools.discord.community

import com.near_reality.tools.discord.*
import com.near_reality.util.capitalize
import com.zenyte.CacheManager
import com.zenyte.game.content.follower.Pet
import com.zenyte.game.content.minigame.inferno.model.InfernoCompletions
import com.zenyte.game.content.serverevent.WorldBoost
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.privilege.GameMode
import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.NewsChannel
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.launch
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import java.util.*
import kotlin.time.Duration.Companion.hours

/**
 * Sends a broadcast message to [DiscordCommunityBot.broadcastChannel].
 *
 * @author Stan van der Bend
 */
fun DiscordCommunityBot.broadcast(player: Player?, broadcastType: BroadcastType, vararg args: Any) {

    if (!DiscordCommunityBot.isInitialized()) {
        logger.info("Ignoring broadcast {} {} {} because bot is not yet initialised.", broadcastType, player, args)
        return
    }

    scope.launch {
        broadcastChannel.runCatching {
            when (broadcastType) {
                BroadcastType.RARE_DROP -> sendRareDrop(player!!, args)
                BroadcastType.LVL_99 -> sendLvl99(player!!, args)
                BroadcastType.MAXED -> sendMaxed(player!!)
                BroadcastType.XP_200M -> sendXp200M(player!!, args)
                BroadcastType.PET -> sendPet(player!!, args)
                BroadcastType.GAMBLE_FIRECAPE -> sendTzokPet(player!!)
                BroadcastType.HCIM_DEATH -> sendHCIMDeath(player!!, args)
                BroadcastType.GROUP_HCIM_DEATH -> sendGroupHCIMDeath(player!!, args)
                BroadcastType.MYSTERY_BOX_RARE_ITEM -> sendBoxLoot(player!!, args, broadcastType)

                BroadcastType.INFERNO_COMPLETION -> sendInfernoCompletion(player!!)
                BroadcastType.TREASURE_TRAILS -> sendTreasureTrailsReward(player!!, args)
                BroadcastType.WILDERNESS_EVENT -> sendGanodermicBeast(args)
                BroadcastType.XAMPHUR -> sendXamphur()
                else -> logger.debug("Ignored broadcast type {}", broadcastType)
            }
        }.onFailure {
            logger.error("Failed to handle {} with contents {}", broadcastType, args, it)
        }
    }.invokeOnCompletion {
        if(it != null)
            logger.error("Failed to handle {} with contents {}", broadcastType, args, it)
    }
}

fun DiscordCommunityBot.onVotesMilestone(player: Player, votes: Int, votesTillSpawn: Int) {
    if (!DiscordCommunityBot.isInitialized()) {
        logger.info("Ignoring broadcast {} {} {} because bot is not yet initialised.", "VoteMilestone", player, votes)
        return
    }
    scope.launch {
        broadcastChannel.runCatching {
            createMessage {
                embed {
                    title = "$votesTillSpawn votes till Xamphur spawns!"
                    description = thanksDescription(player, "**${player.name}** claimed $votes votes.")
                    color = Color(164, 244, 220)
                    thumbnail {
                        url = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/2a4f6754-4bb1-4adb-034d-9503f0ceb400/public"
                    }
                }
                actionRow {
                    linkButton("https://near-reality.com/vote") {
                        label = "Vote here"
                    }
                }
            }
        }.onFailure {
            logger.error("Failed to handle {} with votes {}", player.name, votes, it)
        }
    }.invokeOnCompletion {
        if(it != null)
            logger.error("Failed to handle {} with votes {}", player.name, votes, it)
    }
}

fun DiscordCommunityBot.boostStart(worldBoost: WorldBoost) {
    if (!DiscordCommunityBot.isInitialized()) {
        logger.info("Ignoring broadcast {} because bot is not yet initialised.", "WorldBoost")
        return
    }
    val image = getImageUrl(worldBoost)
    val duration = worldBoost.durationHours.hours
    scope.launch {
        broadcastChannel.runCatching {
            createEmbed {
                title = "World Boost Started"
                description = worldBoost.boostType.mssg
                color = Color(3, 181, 80)
                if (image != null)
                    thumbnail { url = image }
                field("Duration") { duration.toString() }
            }
        }.onFailure {
            logger.error("Failed to handle {}", worldBoost, it)
        }
    }.invokeOnCompletion {
        logger.error("Failed to handle {}", worldBoost, it)
    }
}


fun DiscordCommunityBot.boostEnd(worldBoost: WorldBoost) {
    if (!DiscordCommunityBot.isInitialized()) {
        logger.info("Ignoring broadcast {} because bot is not yet initialised.", "BoostEnd")
        return
    }
    val image = getImageUrl(worldBoost)
    scope.launch {
        broadcastChannel.runCatching {
            createEmbed {
                title = "World Boost Ended"
                description = worldBoost.getBoostType().mssg
                color = Color(176, 28, 35)
                if (image != null)
                    thumbnail { url = image }
            }
        }.onFailure {
            logger.error("Failed to handle {}", worldBoost, it)
        }
    }.invokeOnCompletion {
        logger.error("Failed to handle {}", worldBoost, it)
    }
}

private fun getImageUrl(worldBoost: WorldBoost) = when (worldBoost.getBoostType()) {
    /*XamphurBoost.EXP_BOOST_50 ->
        World.getTemporaryAttributes()["BOOST_SKILL"]?.toString()
            ?.toIntOrNull()
            ?.let { skillImageUrl(formattedSkillName(it)) }*/
    XamphurBoost.RC_RUNES_X2 ->
        skillImageUrl(formattedSkillName(SkillConstants.RUNECRAFTING))

    XamphurBoost.SLAYER_POINTS_X2,
    XamphurBoost.BONUS_SLAYER_SUPERIOR,
    -> skillImageUrl(formattedSkillName(SkillConstants.SLAYER))
//    XamphurBoost.FASTER_FISHING_50PCNT ->
//        skillImageUrl(formattedSkillName(SkillConstants.FISHING))

    XamphurBoost.BONUS_BLOOD_MONEY ->
        itemInventoryImageUrl(ItemId.BLOOD_MONEY)

//    XamphurBoost.BONUS_SEEDS_MSTR_FARMER_50PCNT ->
//        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/41fdaa6e-d16f-45d9-9e75-0876918f7500/public"

    XamphurBoost.LARRANS_KEY_DROPS_X2 ->
        itemInventoryImageUrl(ItemId.LARRANS_KEY)

//    XamphurBoost.VOTE_LOYALTY_X2 ->
//        itemInventoryImageUrl(22561)

    XamphurBoost.BRIMSTONE_KEY_DROPS_X2 ->
        itemInventoryImageUrl(ItemId.BRIMSTONE_KEY)

//    XamphurBoost.MARKS_OF_GRACE_X2 ->
//        itemInventoryImageUrl(ItemId.MARK_OF_GRACE)

    XamphurBoost.GOLDEN_NUGGETS_X2 ->
        itemInventoryImageUrl(ItemId.GOLDEN_NUGGET)

    XamphurBoost.BONUS_CLUE_LOOT ->
        itemInventoryImageUrl(ItemId.CASKET_HARD)

    XamphurBoost.BONUS_BARROWS_DR_25 ->
        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/de2b685d-54a7-4f67-f6d0-ddca0c3f2600/public"

    XamphurBoost.BONUS_ZALCANO_LOOT ->
        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/c240c03f-8b28-49b0-38aa-0786e5392c00/public"

//    XamphurBoost.BONUS_GAUNTLET ->
//        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/cc1c940b-d1ab-4d2f-d7db-e75d2d6d1000/public"

    XamphurBoost.BONUX_COX_POINTS_25PCNT ->
        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/28ae5a41-ad3d-4bd6-8f8e-2a5580e82300/public"

    XamphurBoost.BONUS_PET_RATES ->
        "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/10e92f65-2a8d-45e1-4f35-54814fceb100/public"
    else ->
        null
}
private suspend fun NewsChannel.sendXamphur() = createEmbed {
    title = "Xamphur"
    image = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/6162aafa-e37f-45b1-147e-ff49ac62b600/public"
    color = Color(164, 244, 220)
    description = "Xamphur has awakened from a deep slumber, " +
            "test your skills by facing him at ::event!"
}

private suspend fun NewsChannel.sendGanodermicBeast(args: Array<out Any>) = createEmbed {
    title = "Ganodermic Beast"
    image = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/8f680193-25ff-4e5a-f84e-9704a1676f00/public"
    color = Color(51, 92, 129)
    description = "The Ganodermic Beast's plan for world destruction is being put into motion!"
    field("Location") {
        args[0].toString().substringBeforeLast('<').substringAfter('>')
    }
}

private suspend fun NewsChannel.sendTreasureTrailsReward(
    player: Player,
    args: Array<out Any>,
) = createEmbed {
    val id = args[0] as Int
    val name = ItemDefinitions.getOrThrow(id).name
    val tier = args[1] as String
    title = "Treasure Trails Reward"
    color = Color(196, 155, 90)
    description = buildString {
        append("**${player.name}** has received ")
        if (name.contains("gloves") || name.contains("vambraces")
            || name.contains("gauntlets") || name.contains("boots")
            || name.contains("manacles") || name.contains("sandals")
            || name.contains("legs")
        )
            append("a pair of $name")
        else
            append("${Utils.getAOrAn(name)} $name")
        append(lucky(player))
    }
    field("Tier", true) { tier.capitalize() }
    field("Completed", true) {
        player.getNumericAttribute("completed $tier treasure trails").toString()
    }
    setItemImageThumbnail(id)
}

private suspend fun NewsChannel.sendInfernoCompletion(player: Player) = createEmbed {
    val mode = player.gameMode
    val completionsString = when (InfernoCompletions.getCompletions(mode)) {
        1 -> "first"
        2 -> "second"
        3 -> "third"
        else -> ""
    }
    title = "Inferno Completion!"
    image = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/ed7f490f-d6a5-4911-15ed-ffad8c525b00/public"
    color = Color(122, 62, 74)
    description = buildString {
        append("**${player.name}**")
        if (!InfernoCompletions.isBroadcasted(player) && completionsString.isNotEmpty())
            append(" is the $completionsString to complete the Inferno!")
        else
            append(" has completed the Inferno!")
        append(congratulate(player))
    }
    field("**${player.name}**'s combat level") { player.combatLevel.toString() }
}

private suspend fun NewsChannel.sendBoxLoot(player: Player, args: Array<out Any>, broadcastType: BroadcastType) =
    createEmbed {
        val itemId = args[0] as Int
        val item = Item(itemId)
        val boxName = args[1] as String
        title = "Rare Box Reward"
        color = Color(254, 93, 38)
        description =
            luckyDescription(player, "**${player.name}** has found ${Utils.getAOrAn(item.name)} ${item.name}!")
        field("From") {
            buildString {
                append(boxName)
            }
        }
        setItemImageThumbnail(itemId)
    }

private suspend fun NewsChannel.sendHCIMDeath(player: Player, args: Array<out Any>) = createEmbed {
    title = "Hardcore Ironman Death"
    thumbnail {
        url = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/7768c644-c829-4fff-7bdd-fdd7f77afa00/public"
    }
    color = Color(109, 13, 13)
    description = buildString {
        append("**${player.name}** has died as a Hardcore ${player.appearance.gender}")
        append(" with a skill total of ${player.skills.totalLevel}.")
        append(pity(player))
    }
    val cause = args.firstOrNull()
    addCauseOfDeathField(cause, player)
}

private suspend fun NewsChannel.sendGroupHCIMDeath(player: Player, args: Array<out Any>) = createEmbed {
    title = "Group Hardcore Ironman Death"
    thumbnail {
        url = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/7768c644-c829-4fff-7bdd-fdd7f77afa00/public"
    }
    color = Color(109, 13, 13)
    val cause = args.getOrNull(0)
    val remainingLives = (args.getOrNull(1) as? Int)?:0
    val groupName = args.getOrNull(2)
    description = buildString {
        append("**${player.name}** has died in Hardcore group **$groupName**")
        if (remainingLives > 0) {
            append("The group has **$remainingLives** remaining lives!")
        } else
            append("The group is out of lives and has been reverted back to a regular ironman group.")
        append(pity(player))
    }
    addCauseOfDeathField(cause, player)
}

private fun EmbedBuilder.addCauseOfDeathField(
    cause: Any?,
    player: Player
) {
    field("Cause of death") {
        cause?.let {
            when (it) {
                is Player ->
                    if (it == player)
                        "Self-inflicted damage!"
                    else
                        "Killed by **${it.name}**!"

                is NPC -> "Killed by **${it.definitions.name}**!"
                else -> null
            }
        } ?: "Unknown :o"
    }
}

private suspend fun NewsChannel.sendTzokPet(player: Player) = createEmbed {
    title = "TzRek-Jad pet"
    color = Color(139, 30, 63)
    description =
        luckyDescription(player, "**${player.name}** has received the TzRek-Jad pet by gambling their fire cape!")
    setItemImageThumbnail(ItemId.TZREKJAD)
}


private suspend fun NewsChannel.sendPet(player: Player, args: Array<out Any>) = createEmbed {
    val pet = args[0] as Pet
    val petName = NPCDefinitions.get(pet.petId()).name
    val petItemId = pet.itemId()
    title = "Pet"
    color = Color(135, 132, 114)
    description = luckyDescription(player, "**${player.name}** has received the **$petName** pet!")
    setItemImageThumbnail(petItemId)
}

private suspend fun NewsChannel.sendXp200M(
    player: Player,
    args: Array<out Any>,
) = createEmbed {
    val skill = args[0] as Int
    val skillName = formattedSkillName(skill)
    title = "200M Experience Achievement"
    color = Color(55, 102, 109)
    description =
        congratulatoryDescription(player, "**${player.name}** has achieved 200m experience in **$skillName**!")
    thumbnail {
        url = skillImageUrl(skillName)
    }
    gameAndXpModeField(player)
}

private suspend fun NewsChannel.sendMaxed(player: Player) = createEmbed {
    title = "Maxed Achievement"
    description = congratulatoryDescription(player, "**${player.name}** has achieved level 99 in all skills!")
    gameAndXpModeField(player)
    setItemImageThumbnail(ItemId.MAX_CAPE)
}

private suspend fun NewsChannel.sendLvl99(player: Player, args: Array<out Any>) = createEmbed {
    val skill = args[0] as Int
    val skillName = formattedSkillName(skill)
    title = "Level 99 Achievement"
    color = Color(127, 183, 190)
    description = congratulatoryDescription(player, "**${player.name}** has achieved level 99 in **$skillName**!")
    thumbnail {
        url = skillImageUrl(skillName)
    }
    gameAndXpModeField(player)
}

private fun skillImageUrl(skillName: String) = "https://oldschool.runescape.wiki/images/${skillName}.png"


private suspend fun NewsChannel.sendRareDrop(player: Player, args: Array<out Any>) = createEmbed {
    val item = args[0] as Item
    val source = args[1] as String
    title = "Rare Drop"
    description =
        luckyDescription(player, "**${player.name}** has received ${Utils.getAOrAn(item.name)} **${item.name}** drop!")
    color = Color(163, 247, 181)
    setItemImageThumbnail(item.id)
    field("Source", true) {
        buildString {
            if (source.endsWith("Chambers of Xeric"))
                append(
                    "From **$source** on chest ${
                        player.getNumericAttribute(if (source.startsWith("Challenge Mode")) "challengechambersofxeric" else "chambersofxeric")
                              .toInt()
                    }"
                )
            else {
                if (source.startsWith("."))
                    append("From ").append(source.substring(1))
                else {
                    append("From ${Utils.getAOrAn(source)} **$source**")
                    if (NotificationSettings.isKillcountTracked(source))
                        append(" on killcount ${player.notificationSettings.getKillcount(source) + 1}")
                }
            }
        }
    }
}

private fun EmbedBuilder.gameAndXpModeField(player: Player) {
    field("Game mode", true) {
        player.gameMode.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
    field("XP mode", true) { "${player.combatXPRate}/${player.skillingXPRate}" }
}

private fun formattedSkillName(skill: Int) = Skills.getSkillName(skill)
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

private fun EmbedBuilder.setItemImageThumbnail(itemId: Int) {
    thumbnail {
        url = itemInventoryImageUrl(itemId)
    }
}

fun main() {
    CacheManager.loadCache(Cache.openCache("base/cache/data/cache", true))
    CacheManager.loadDefinitions()
    EnumDefinitions().load()

    DiscordCommunityBot.init(
        DiscordServer.Main,
        token = "MTAxOTY0MTYxNTE4ODUwODgxNg.GoD34n.27s6pG87WITl8rN9r1S25a1z76BNyWo28trlIg"
    )
    val player = mockk<Player>()
    every { player.name } returns "Stan"
    every { player.getNumericAttribute(any()) } returns 1
    every { player.notificationSettings.getKillcount(any()) } returns 0
    every { player.gameMode } returns GameMode.REGULAR
    every { player.combatXPRate } returns 250
    every { player.skillingXPRate } returns 80
    every { player.appearance.gender } returns "Man"
    every { player.skills.totalLevel } returns 420
    every { player.combatLevel } returns 69

    val attributes = mutableMapOf<String, Any?>()
    attributes["discordUserId"] = 319978682016071681L
    every { player.attributes } returns attributes

    mockkStatic(InfernoCompletions::getCompletions)
    mockkStatic(InfernoCompletions::isBroadcasted)
    every { InfernoCompletions.getCompletions(any()) } returns 1
    every { InfernoCompletions.isBroadcasted(any()) } returns true

//    DiscordCommunityBot.broadcast(player, BroadcastType.RARE_DROP, Item(ItemId.DRAGON_KITE), "King Black Dragon")
//    DiscordCommunityBot.broadcast(player, BroadcastType.LVL_99, SkillConstants.AGILITY)
//    DiscordCommunityBot.broadcast(player, BroadcastType.LVL_99, SkillConstants.ATTACK)
//    DiscordCommunityBot.broadcast(player, BroadcastType.MAXED)
//    DiscordCommunityBot.broadcast(player, BroadcastType.XP_200M, SkillConstants.PRAYER)
//    DiscordCommunityBot.broadcast(player, BroadcastType.PET, BossPet.NEXLING)
//    DiscordCommunityBot.broadcast(player, BroadcastType.GAMBLE_FIRECAPE)
//    DiscordCommunityBot.broadcast(player, BroadcastType.HCIM_DEATH, player)
//    DiscordCommunityBot.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, 11862)
//    DiscordCommunityBot.broadcast(player, BroadcastType.INFERNO_COMPLETION)
//    DiscordCommunityBot.broadcast(player, BroadcastType.TREASURE_TRAILS, ItemId.RING_OF_3RD_AGE, "elite")
//    DiscordCommunityBot.broadcast(player, BroadcastType.GANODERMIC_BEAST, "yo momma")
//    DiscordCommunityBot.broadcast(player, BroadcastType.XAMPHUR)
//
    DiscordCommunityBot.onVotesMilestone(player, 10, 90)
//
//    Thread.sleep(1000L)
//    for (type in XamphurBoost.values()) {
//        World.getTemporaryAttributes()["BOOST_SKILL"] = SkillConstants.MAGIC
//        val boost = WorldBoost(type, Clock.System.now().plus(1.hours).toEpochMilliseconds())
//        DiscordCommunityBot.boostStart(boost)
//        Thread.sleep(200L)
////        DiscordCommunityBot.boostEnd(boost)
//    }
}
