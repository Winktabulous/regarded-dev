package com.near_reality.game.world.entity.player

import com.near_reality.game.content.bountyhunter.BountyHunterWildernessRange
import com.near_reality.game.util.Ticker
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.Trade
import com.zenyte.game.world.entity.player.container.impl.death.DeathMechanics
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.entity.weakReferenceAttribute


/**
 * The id of the discord user linked to this account.
 */
var Player.discordUserId by persistentAttribute<Long?>("discordUserId", null)
var Player.disableDiscordLinkRequests by persistentAttribute("disableDiscordLinkRequests", false)
var Player.killingBlowHit: Hit? by attribute("killingBlowHit", null)
var Player.selectedGameMode: GameMode by attribute("selected_game_mode", GameMode.REGULAR)
var Player.selectedGameModeDifficulty by persistentAttribute("selected_game_mode_difficulty", 0)

var Player.selectedUniversalShopCategory: Int by attribute("selected_universal_shop_category", 0)
var Player.univShopSearchActive: Boolean by attribute("univ_shop_search_active", false)
var Player.univShopDoubleProcess: Boolean by attribute("univShopDoubleProcess", false)

var Player.flaggedAsBot: Boolean by persistentAttribute("flaggedAsBot", false)

/* Bounty Hunter Start */

var Player.bountyHunterPoints: Int by persistentAttribute("bountyHunterPoints", 0)
var Player.bountyHunterCurrentWildernessRange: BountyHunterWildernessRange? by attribute("bountyHunterWildernessRange", null)
var Player.bountyAbandonedTicker: Ticker by attribute("bhTicker", Ticker(100, active = false, resetAutomatically = true, defaultsToInactive = true))
var Player.bountyHunterInfoDisplay: Int by persistentAttribute("bountyHunterInfoDisplayIdx", 0)
var Player.bountyHunterInterfaceRateLimit: Int by attribute("bountyHunterRateLimit", 0)
var Player.bountyHunterInfoCooldown: Int by attribute("bountyHunterInfoCooldown", 0)
var Player.bountyHunterKills: Int by persistentAttribute("bountyHunterKills", 0)
var Player.bountyHunterDeaths: Int by persistentAttribute("bountyHunterDeaths", 0)
var Player.bountyHunterKillstreak: Int by persistentAttribute("bountyHunterKillstreak", 0)
var Player.bountyHunterSkipCount: Int by persistentAttribute("bountyHunterSkipCount", 0)
var Player.bountyHunterLastTarget: String by persistentAttribute("bountyHunterLastTarget", "")

/* Bounty Hunter End */

var Player.sacrificedTwistedBow: Boolean by persistentAttribute("sacrificedTwistedBow", false)
var Player.sacrificedScytheOfVitur: Boolean by persistentAttribute("sacrificedScytheOfVitur", false)
var Player.sacrificedTumekensShadow: Boolean by persistentAttribute("sacrificedTumekensShadow", false)

var Player.manuallyLeftHelpChat: Boolean by persistentAttribute("manuallyLeftHelpChat", false)
var Player.exchangePoints: Int by persistentAttribute("exchangePoints", 0)
var Player.boonRuneStored: Int by persistentAttribute("boonRuneStored", null)
var Player.boonQuantStored: Int by persistentAttribute("boonQuantStored", 0)

var Player.toaPetAkkhito: Boolean by persistentAttribute("toa-pet-akkha", false)
var Player.toaPetBabi: Boolean by persistentAttribute("toa-pet-baba", false)
var Player.toaPetKephriti: Boolean by persistentAttribute("toa-pet-kephri", false)
var Player.toaPetZebo: Boolean by persistentAttribute("toa-pet-zebak", false)
var Player.toaPetRemnant: Boolean by persistentAttribute("toa-pet-remnant", false)
var Player.dailyRemainingTomes: Int by persistentAttribute("exchange-daily-tomes-remaining", 0)
var Player.migrationVersion: Int by persistentAttribute("nr-migration-version", 0)

var Player.tormentedDemonAccuracyBoost: Boolean by persistentAttribute("tormentedDemonAccuracyBoost", false)

/**
 * TODO: bad design, future improvements to plugin system are needed.
 */
var Player.ironGroupTradeAddItemCheck: (Trade.(Item) -> Boolean)? by attribute(
    "ironGroupTradeAddItemCheck",
    null
)

/**
 * TODO: bad design, future improvements to plugin system are needed.
 */

var Player.hardcoreIronGroupDeathHandlingOverride: (DeathMechanics.(Player, Entity?) -> Unit)? by attribute(
    "hardcoreIronGroupDeathHandlingOverride",
    null
)

var Player.ironGroupMessageHandler: ((message: String, name: String) -> Unit)? by attribute(
    "ironGroupMessageHandler",
    null
)

var Player.boneCrusherNecklaceActivationTime : Long by attribute("boneCrusherNecklaceActivationTime", 0L)

var Player.freezeCaster: Entity? by weakReferenceAttribute("freezeCaster")
var Player.pvmArenaPoints: Long by persistentAttribute("pvmArenaPoints", 0L)
var Player.pvmArenaPointsGainedDuringGame: Long by attribute("pvmArenaPointsGainedInMatch", 0L)
var Player.pvmArenaMvpCountDuringGame: Long by attribute("pvmArenaMvpCountInMatch", 0L)

/**
 * Used to display a blue or red icon beneath the player, which is an item shown in the beard slot.
 */
var Player.pvmArenaAppearanceBeardOffset: Int by attribute("pvmArenaAppearanceBeardOffset", 0)

var Player.pvmArenaRevivalCount: Int by attribute("pvmArenaRevivalCount", 0)

var Player.pvmArenaInRevivalState: Boolean by attribute("pvmArenaInRevivalState", false)

var Player.pvpKills : Int by persistentAttribute("pvp-kills", 0)
var Player.pvpDeaths : Int by persistentAttribute("pvp-deaths", 0)
var Player.pvpKillStreak: Int by persistentAttribute("current-killstreak", 0)

var Player.wildernessResourceAreaPaidFeeAmount: Int by attribute("wildernessResourceAreaPaidFeeAmount", 0)

var Player.blackSkulled : Boolean by persistentAttribute("blackSkulled", false)
var Player.sanityValue by attribute("dt2_whispy_sanity", 100)
