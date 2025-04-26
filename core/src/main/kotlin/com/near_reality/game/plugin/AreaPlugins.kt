package com.near_reality.game.plugin

import com.near_reality.game.util.inWholeTicks
import com.zenyte.game.content.skills.magic.spells.MagicSpell
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.area.plugins.*
import kotlin.reflect.KClass
import kotlin.time.Duration

/*
NOTE: interface delegation does not work if the underlying interface is in java and has a default impl.
 */

fun safeDeaths(
    information: String,
    respawnLocationProvider: () -> Location,
) = object : DeathPlugin {
    override fun isSafe(): Boolean = true
    override fun getDeathInformation(): String = information
    override fun getRespawnLocation(): Location = respawnLocationProvider()
}

fun tradingDisabled(message: Boolean = true) = TradePlugin { player, _ ->
    if (message)
        player?.sendMessage(Colour.RS_RED.wrap("Trading is disabled in this area."))
    false
}

fun experienceGainDisabled() = ExperiencePlugin { false }

fun spellCastingDisabled(message: Boolean = true) = SpellPlugin { player, _ ->
    if (message)
        player?.sendMessage(Colour.RS_RED.wrap("You may not cast spells in this area."))
    false
}

fun teleportingDisabled(message: Boolean = true) = TeleportPlugin { player, _ ->
    if (message)
        player?.sendMessage(Colour.RS_RED.wrap("You may not teleport from this area."))
    false
}

fun onLogout(action: (Player) -> Unit) = LogoutPlugin { player ->
    action(player)
}

fun loggingOutDisabled() = LogoutRestrictionPlugin {
    it.sendMessage(Colour.RS_RED.wrap("You may not logout here."))
    false
}

fun prayersDisabled(
    vararg disabledPrayers: Prayer,
) = object : PrayerPlugin {
    override fun activatePrayer(player: Player?, prayer: Prayer?): Boolean {
        if (disabledPrayers.contains(prayer)) {
            player?.sendMessage(Colour.RS_RED.wrap("You may not use this prayer here."))
            return false
        }
        return true
    }
}

fun spellsDisabled(
    vararg disabledSpells: KClass<out MagicSpell>,
) = object : SpellPlugin {
    override fun canCast(player: Player?, spell: MagicSpell?): Boolean {
        spell ?: return false
        if (disabledSpells.contains(spell::class)) {
            player?.sendMessage(Colour.RS_RED.wrap("You may not cast this spell here."))
            return false
        }
        return true
    }
}

/**
 * Only shows the dropped item to the player who dropped it for the specified duration.
 * After the duration, the item will be removed from the ground.
 */
fun droppedItemsDontAppearGlobally(
    visibleDuration: Duration,
) = object : IDropPlugin {
    override fun drop(player: Player?, item: Item?): Boolean = true
    override fun dropOnGround(player: Player?, item: Item?): Boolean = true
    override fun visibleTicks(player: Player?, item: Item?): Int = 0
    override fun invisibleTicks(player: Player?, item: Item?): Int = visibleDuration.inWholeTicks.toInt()
}

fun droppedItemsVanishInstantly() = object : IDropPlugin {


    override fun drop(player: Player?, item: Item?): Boolean = true
    override fun dropOnGround(player: Player?, item: Item?): Boolean {
        player?.sendMessage(Colour.RS_RED.wrap("The " + item?.name + " vanishes as it touches the ground."))
        return false
    }
    override fun visibleTicks(player: Player?, item: Item?): Int = 200
    override fun invisibleTicks(player: Player?, item: Item?): Int = 100
}
