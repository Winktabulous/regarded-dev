package com.near_reality.game.content.tournament.area

import com.near_reality.game.content.tournament.Tournament
import com.near_reality.game.content.tournament.TournamentState
import com.near_reality.game.content.tournament.moveToTournamentPortal
import com.near_reality.game.content.tournament.preset.TournamentPreset
import com.near_reality.game.content.tournament.sendTournamentMessage
import com.near_reality.game.plugin.experienceGainDisabled
import com.near_reality.game.plugin.prayersDisabled
import com.near_reality.game.plugin.spellsDisabled
import com.near_reality.game.plugin.tradingDisabled
import com.near_reality.game.util.formattedString
import com.zenyte.game.content.skills.magic.spells.lunar.NPCContact
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.area.plugins.*
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import kotlin.time.Duration.Companion.seconds

abstract class TournamentArea(
    preset: TournamentPreset,
    allocatedArea: AllocatedArea,
    staticChunkX: Int,
    staticChunkY: Int
) :
    DynamicArea(allocatedArea, staticChunkX, staticChunkY),
    IDropPlugin,
    ExperiencePlugin by experienceGainDisabled(),
    SpellPlugin by spellsDisabled(
        NPCContact::class,
        SpellbookSwap::class
    ),
    LogoutRestrictionPlugin,
    PrayerPlugin by prayersDisabled(*preset.disabledPrayers),
    TempPlayerStatePlugin,
    TradePlugin by tradingDisabled(message = true),
    CycleProcessPlugin
{
    private var destroying = false

    abstract val tournament: Tournament

    override fun process() = Unit

    override fun postProcess() {
        if (destroying)
            return
        val state = tournament.state as? TournamentState.Finished ?:return
        val autoDestroyRegionTimer = state.autoDestroyRegionTimer
        if (autoDestroyRegionTimer.elapsed()) {
            players.toSet().forEach { it.moveToTournamentPortal() }
            destroying = true
            destroyRegion()
        } else {
            autoDestroyRegionTimer.every(10.seconds) {
                players.forEach { it.sendTournamentMessage("You'll be moved back home in ${autoDestroyRegionTimer.durationRemaining().formattedString}.") }
            }
        }
    }
}
