package com.near_reality.game.content.tournament.area

import com.near_reality.game.content.tournament.*
import com.near_reality.game.content.tournament.area.TournamentLobbyArea.Companion.LOBBY_CENTER
import com.near_reality.game.content.tournament.loc.TournamentPortalObject
import com.near_reality.game.content.tournament.npc.TournamentGuardLobbyPlugin
import com.near_reality.game.plugin.safeDeaths
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.RandomLocation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.area.plugins.*
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.plugins.equipment.equip.EquipPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class TournamentLobbyArea(
    allocatedArea: AllocatedArea,
    override val tournament: Tournament,
) :
    TournamentArea(tournament.preset, allocatedArea, 416, 912),
    DeathPlugin by safeDeaths(
        information = "Items lost inside tournament-restricted areas are erased when the user leaves the tournament, which includes dying.",
        respawnLocationProvider = { RESPAWN_LOCATION } // actually handled by #getRespawnLocation to apply the instance shift
    ),
    FullMovementPlugin,
    TeleportPlugin,
    TeleportMovementPlugin,
    EquipmentPlugin
{
    private val log: Logger = LoggerFactory.getLogger("TournamentLobbyArea(preset=${tournament.preset}, center=${allocatedArea.centerLocation.regionId})")

    internal val playersInFightWaitingArea: List<Player>
        get() = players.filter { inFightWaitingArea(it.x, it.y)}

    internal fun teleportPlayer(player: Player) {
        player.lock(1)
        player.setLocation(getLocation(RESPAWN_LOCATION))
    }

    override fun constructed() {
        TournamentGuardLobbyPlugin.spawn(area = this)
    }

    override fun enter(player: Player) {
        if (!player.appliedTournamentPresetToPlayer) {
            tournament.preset.apply(player)
            player.appliedTournamentPresetToPlayer = true
        } else
            player.tournamentPresetSaved?.apply(player)
        player.interfaceHandler.closeInterface(InterfacePosition.MINIGAME_OVERLAY)
        SpellbookSwap.checkSpellbook(player)
    }

    override fun cleared() {
        if (tournament.state is TournamentState.Finished) {
            if (players.isEmpty()) {
                log.info("Tournament is finished, destroying regions.")
                destroyRegion()
            } else
                log.info("Tournament is finished, but there are still players in the lobby.")
        } else
            log.info("Tournament is not finished, not destroying regions.")
    }

    override fun leave(player: Player, logout: Boolean) {
        if (isNotInFightAreaOfThisTournament(player.location)) {
            player.appliedTournamentPresetToPlayer = false
            log.info("Removed {} from lobby players, now has {} remaining.", player, playersInFightWaitingArea.size)
            closeTournamentInterfaceOverlays(player)
            if (logout && player.isFinished)
                player.forceLocation(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL.copy())
        }
    }

    override fun processMovement(player: Player?, x: Int, y: Int): Boolean {
        player?:return true
        checkIfInFightingArea(player, x, y)
        return true
    }

    override fun processMovement(player: Player?, destination: Location?) {
        player?:return
        destination?:return
        checkIfInFightingArea(player, destination.x, destination.y)
    }

    private fun checkIfInFightingArea(player: Player, x: Int, y: Int) {
        if (isNotInFightAreaOfThisTournament(Location(x, y))) {
            if (inFightWaitingArea(player) && !inFightWaitingArea(x, y)) {
                player.sendDeveloperMessage("You have left the fight waiting area.")
                tournament.removeFromCombatPairs(player, death = false)
                removeOverlaysAndResetState(player)
                removeFloorItemsInFightingWaitArea(player)
            }
        }
    }

    private fun isNotInFightAreaOfThisTournament(location: Location): Boolean {
        val area = GlobalAreaManager.getArea(location) as? TournamentArea
        return area !is TournamentFightArea || area.tournament != tournament
    }

    override fun name(): String =
        "Tournament Lobby"

    override fun onLoginLocation(): Location =
        RandomLocation.random(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL, 1)

    override fun getRespawnLocation(): Location =
        getLocation(RESPAWN_LOCATION)

    override fun canTeleport(player: Player, teleport: Teleport): Boolean =
        !inFightWaitingArea(player)

    override fun manualLogout(player: Player?): Boolean {
        if (inFightWaitingArea(player?:return true)) {
            player.sendTournamentMessage("Please first leave the fight wait area before logging out..")
            return false
        } else
            return true
    }
    override fun enableTempState(location: Location, type: TempPlayerStatePlugin.StateType): Boolean =
        inFightWaitingArea(location)

    override fun isMultiwayArea(position: Position): Boolean =
        true

    override fun equip(player: Player, item: Item, slot: Int): Boolean =
        inFightWaitingArea(player.location)

    override fun unequip(player: Player, item: Item, slot: Int): Boolean =
        inFightWaitingArea(player.location)

    override fun drop(player: Player?, item: Item?): Boolean = true
    override fun dropOnGround(player: Player?, item: Item?): Boolean {
        player?.sendMessage("The " + item?.name + " vanishes as it touches the ground.")
        return false
    }
    override fun visibleTicks(player: Player?, item: Item?): Int = 200
    override fun invisibleTicks(player: Player?, item: Item?): Int = 100

    internal companion object {
        val LOBBY_CENTER = Location(3358, 7460, 0)
        val RESPAWN_LOCATION: Location = Location(3358, 7445, 0)
        val SPECTATING_LOCATION: Location = Location(3358, 7446, 0)
        val WINNER_LOCATION: Location = Location(3359, 7450, 0)
    }
}


private const val MIN_X = 3351
private const val MAX_X = 3366
private const val MIN_Y = 7453
private const val MAX_Y = 7468

private fun TournamentLobbyArea.removeFloorItemsInFightingWaitArea(player: Player) {
    var x = getX(MIN_X)
    while (x <= getX(MAX_X)) {
        var y = getY(MIN_Y)
        while (y <= getY(MAX_Y)) {
            val chunk = World.getChunk(x, y, 0)
            val items = chunk.floorItems
            if (items == null) {
                y += 8
                continue
            }
            val itemList = ArrayList(items)
            for (item in itemList) {
                if (item.isVisibleTo(player) && inFightWaitingArea(item.location.x, item.location.y)) {
                    World.destroyFloorItem(item)
                }
            }
            y += 8
        }
        x += 8
    }
}

private fun TournamentLobbyArea.inFightWaitingArea(x: Int, y: Int): Boolean =
    x >= getX(MIN_X) && x <= getX(MAX_X) && y >= getY(MIN_Y) && y <= getY(MAX_Y)

private fun TournamentLobbyArea.inFightWaitingArea(position: Position): Boolean =
    inFightWaitingArea(position.position.x, position.position.y)

internal val TournamentLobbyArea.randomFightingWaitAreaLocation: Location
    get() {
        var maxAttempts = 100
        val center = getLocation(LOBBY_CENTER)
        while (--maxAttempts > 0) {
            val tile = RandomLocation.random(center, 10)
            if (inFightWaitingArea(tile.x, tile.y) && World.isFloorFree(tile, 1)) {
                return tile
            }
        }
        return center.transform(0, -2, 0)
    }
