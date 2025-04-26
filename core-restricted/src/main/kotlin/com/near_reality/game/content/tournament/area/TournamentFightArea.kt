package com.near_reality.game.content.tournament.area

import com.near_reality.game.content.tournament.*
import com.near_reality.game.plugin.droppedItemsDontAppearGlobally
import com.near_reality.game.plugin.loggingOutDisabled
import com.near_reality.game.plugin.onLogout
import com.near_reality.game.plugin.teleportingDisabled
import com.zenyte.game.item.Item
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Setting
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.area.plugins.*
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.minutes

/**
 * Represents an instanced area for the argued [tournament].
 *
 * @author Kris | 26/05/2019 19:56
 * @author Stan van der Bend
 */
class TournamentFightArea private constructor(
    override val tournament: Tournament,
    private val pairs: Queue<TournamentPair>,
    allocatedArea: AllocatedArea,
) : TournamentArea(tournament.preset, allocatedArea, 418, 914),
    DeathPlugin,
    EntityAttackPlugin,
    LogoutPlugin by onLogout(tournament::removeFromCombatPairs),
    LogoutRestrictionPlugin by loggingOutDisabled(),
    TeleportPlugin by teleportingDisabled(message = true)
{
    private val logger = LoggerFactory.getLogger("TournamentFightArea(tournament=${tournament.preset}, center=${allocatedArea.centerLocation.regionId})")

    private val dropHandler = droppedItemsDontAppearGlobally(visibleDuration = 1.minutes)
    override fun drop(player: Player?, item: Item?): Boolean = dropHandler.drop(player, item)
    override fun dropOnGround(player: Player?, item: Item?): Boolean = dropHandler.dropOnGround(player, item)
    override fun visibleTicks(player: Player?, item: Item?): Int = dropHandler.visibleTicks(player, item)
    override fun invisibleTicks(player: Player?, item: Item?): Int = dropHandler.invisibleTicks(player, item)

    private val deathHandler = TournamentFightAreaDeathHandler(tournament)
    override fun isSafe(): Boolean = deathHandler.isSafe()
    override fun getRespawnLocation(): Location = deathHandler.getRespawnLocation()
    override fun getDeathInformation(): String = deathHandler.getDeathInformation()
    override fun sendDeath(player: Player?, source: Entity?): Boolean = deathHandler.sendDeath(player, source)

    override fun constructRegion() {
        if (constructed) {
            logger.warn("Region already constructed")
            return
        }
        when(val state = tournament.state) {
            is TournamentState.RoundOver,
            is TournamentState.Scheduled -> {
                logger.info("Constructing region for state: $state")
                GlobalAreaManager.add(this)
                try {
                    val pairs = pairs
                    val count = pairs.size
                    var currentIndex = 0
                    fun copySquare(x: Int, y: Int, plane: Int) =
                        MapBuilder.copySquare(area, 4, staticChunkX, staticChunkY, plane, (x * 4) + area.chunkX, (y * 4) + area.chunkY, plane, 0)
                    loop@ for (x in 0 until getSpan(pairs.size)) {
                        for (y in 0 until getSpan(pairs.size)) {
                            copySquare(x, y, 0)
                            copySquare(x, y, 1)
                            if (++currentIndex >= count)
                                break@loop
                        }
                    }
                } catch (e: OutOfBoundaryException) {
                    logger.error("Failed to construct region for state: $state", e)
                }
                constructed = true
                constructed()
            }
            else ->
                logger.warn("Cannot construct region for state: $state")
        }
    }

    override fun constructed() {
        if (tournament.state is TournamentState.Finished) {
            logger.warn("Region constructed but tournament is finished")
            return
        }
        val count = pairs.size
        logger.info("Constructing {} map chunks", count)
        var currentIndex = 0
        val pairsList = ArrayList(pairs)
        val width = getSpan(pairs.size)
        val height = getSpan(pairs.size)
        loop@ for (x in 0 until width) {
            for (y in 0 until height) {
                logger.info("constructing[{}] ({}, {})", currentIndex, x, y)
                val pair = pairsList[currentIndex]
                logger.info("\tfor pair {}", pair)
                checkNotNull(pair)
                val left = pair.first?:continue
                val right = pair.second?:continue
                val dynamicCorner = Location(
                    (((x * 4) + area.chunkX) shl 3),
                    (((y * 4) + area.chunkY) shl 3),
                    0
                )
                logger.info("\tin corner {}", dynamicCorner)
                val spectatorTile = dynamicCorner.transform(15, 16, 0)
                val leftLocation = dynamicCorner.transform(13, 16, 0)
                val rightLocation = dynamicCorner.transform(18, 16, 0)
                pair.spectatorLocation = spectatorTile
                left.moveToFightZone(leftLocation, right)
                right.moveToFightZone(rightLocation, left)
                if (++currentIndex >= count) {
                    logger.info("Added all players")
                    break@loop
                }
            }
        }
    }

    override fun enter(player: Player) {
        player.sendDeveloperMessage("Entering tournament fight area")
        //Only allow the user to fight if they're not spectating.
        player.findPlayerOption("Trade with").ifPresent { value: Int -> player.setPlayerTradeable(false) }
        player.isCanPvp = player.tournamentPairSpectating == null
        with(player.varManager) {
            sendBit(WildernessArea.IN_WILDERNESS_VARBIT_SPECIAL_UNCLICKABLE, 0)
            sendBit(Setting.RIGOUR.id, 1)
            sendBit(Setting.AUGURY.id, 1)
            sendBit(Setting.PRESERVE.id, 1)
        }
    }

    override fun leave(player: Player, logout: Boolean) {
        player.sendDeveloperMessage("Leaving tournament fight area")
        player.setPlayerTradeable(true)
        player.isCanPvp = false
        player.varManager.sendBit(WildernessArea.IN_WILDERNESS_VARBIT_SPECIAL_UNCLICKABLE, 0)
        player.settings.refresh()
        tournament.removeFromCombatPairs(player, false)
    }

    override fun isMultiwayArea(position: Position): Boolean = true

    override fun onLoginLocation(): Location = Location(3081, 3512, 0)

    override fun name(): String = "Tournament Zone"

    override fun attack(player: Player, entity: Entity, combat: PlayerCombat?): Boolean {
        val state = tournament.state as? TournamentState.RoundActive ?: return false
        if (!state.started) {
            player.sendTournamentMessage("The fight has not started yet!")
            return false
        }
        return true
    }

    override fun enableTempState(location: Location, type: TempPlayerStatePlugin.StateType): Boolean =
        true

    companion object {

        @JvmStatic
        fun build(tournament: Tournament, pairs: Queue<TournamentPair>): TournamentFightArea {
            try {
                val pairCount = pairs.size
                val width = getSpan(pairCount)
                val height = getSpan(pairCount)
                val allocatedArea = MapBuilder.findEmptyChunk(width * 4, height * 4)
                val instance = TournamentFightArea(tournament, pairs, allocatedArea)
                instance.constructRegion()
                return instance
            } catch (e: OutOfSpaceException) {
                throw IllegalStateException(e)
            }
        }

        fun getSpan(size: Int): Int =
            ceil(sqrt(size.toDouble())).toInt()
    }
}
