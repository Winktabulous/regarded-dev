package com.near_reality.game.content.tournament

import com.near_reality.game.content.tournament.area.TournamentArea
import com.near_reality.game.content.tournament.loc.TournamentPortalObject
import com.near_reality.game.content.tournament.preset.TournamentPresetSave
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.varp
import com.zenyte.game.world.entity.weakReferenceAttribute
import java.util.*

internal var Player.tournamentPlayerCount by varp(3611)
internal var Player.tournamentRound by varp(3612)
internal var Player.tournamentTimeInTicks by varp(3613)

internal var Player.appliedTournamentPresetToPlayer: Boolean by attribute("participating_in_tournament", false)
internal var Player.previousBoxingWinner: Boolean by persistentAttribute("participating_in_tournament", false)
internal var Player.tournamentPresetSaved: TournamentPresetSave? by attribute("tournament_preset")
internal var Player.tournamentSpectatorLocation: Location? by attribute("tournament spectating tile")
internal var Player.tournamentPairSpectating: TournamentPair? by weakReferenceAttribute("tournament_spectating")
internal val Player.tournamentPairsAvailableToSpectate: Queue<TournamentPair>?
    get() = (tournamentOrNull?.state as? TournamentState.RoundActive)?.combatPairs
internal val Player.tournamentOrNull
    get() = (area as? TournamentArea)?.tournament

internal fun Player.sendTournamentMessage(message: String)  {
    sendMessage(Colour.RS_RED.wrap(message))
}

internal fun removeOverlaysAndResetState(player: Player) {
    closeTournamentInterfaceOverlays(player)
    player.reset()
}

internal fun closeTournamentInterfaceOverlays(player: Player) {
    player.interfaceHandler.closeInterface(InterfacePosition.WILDERNESS_OVERLAY)
    player.interfaceHandler.closeInterface(InterfacePosition.MINIGAME_OVERLAY)
}

internal fun Player.moveToFightZone(location: Location, other: Player) {
    stop(
        Player.StopType.INTERFACES,
        Player.StopType.ROUTE_EVENT,
        Player.StopType.WALK,
        Player.StopType.ACTIONS,
        Player.StopType.ANIMATIONS,
        Player.StopType.WORLD_MAP
    )
    setLocation(location.copy())
    setFaceEntity(other)
    variables.resetScheduled()
    reset()
    prayerManager.deactivateActivePrayers()
    WorldTasksManager.schedule({
        refreshDirection()
        setFaceEntity(null)
    })
    GameInterface.WILDERNESS_OVERLAY.open(this)
}
internal fun Player.moveToTournamentPortal() {
    setLocation(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL.random(2))
    restoreStatePostFight()
}
internal fun Player.restoreStatePostFight() {
    reset()
    combatDefinitions.specialEnergy = 100
}
