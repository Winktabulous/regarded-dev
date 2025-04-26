package com.near_reality.game.content.tournament

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.content.tournament.area.TournamentFightArea
import com.near_reality.game.content.tournament.area.TournamentLobbyArea
import com.near_reality.game.content.tournament.area.randomFightingWaitAreaLocation
import com.near_reality.game.content.tournament.preset.TournamentPreset
import com.near_reality.game.item.CustomItemId
import com.near_reality.game.util.WorldTimer
import com.near_reality.game.util.formattedString
import com.zenyte.game.content.advent.AdventCalendarManager
import com.zenyte.game.item.Item
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.GameMode
import kotlinx.datetime.Clock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Represents a tournament instance, multiple of which can be active at the same time.
 *
 * @author Kris | 26/05/2019 20:11
 * @author Stan van der Bend
 */
class Tournament(val preset: TournamentPreset, timer: WorldTimer) {

    private val scheduledTime = Clock.System.now() + timer.durationRemaining()
    internal var state: TournamentState = TournamentState.Scheduled(timer)
    internal lateinit var lobby: TournamentLobbyArea
    internal val logger: Logger by lazy {
        LoggerFactory.getLogger("Tournament(preset=${preset}, center=${lobby.area.centerLocation.regionId})")
    }
    internal val participants get() = lobby.playersInFightWaitingArea

    fun tryStartNextRound() {
        val playersInFightWaitingArea = participants
        playersInFightWaitingArea.forEach(::advanceChallengeProgress)
        val participants = playersInFightWaitingArea.toMutableSet()
        val pairs = LinkedList(participants.removePairs())
        val unpairedPlayer = participants.firstOrNull()
        if (pairs.isEmpty()) {
            val started = state !is TournamentState.Scheduled
            if (unpairedPlayer != null && started) {
                win(unpairedPlayer)
            } else {
                if (!started)
                    cancel(reason = "as there are not enough participants.")
                else {
                    state = TournamentState.Finished.NoWinner
                    logger.info("Tournament has ended with no winner.")
                }
            }
            return
        }
        logger.info("Starting with {} participants", playersInFightWaitingArea.size)
        logger.info("Created {} pairs", pairs.size)
        val round = when(val previousState = state) {
            is TournamentState.Scheduled -> 1
            is TournamentState.Ongoing -> previousState.round + 1
            is TournamentState.Finished -> error("Unexpected state: $previousState")
        }
        val fight = TournamentFightArea.build(this, pairs)

        state = TournamentState.RoundActive(fight, round, pairs, unpairedPlayer)
        if (unpairedPlayer != null)
            moveToNextRound(unpairedPlayer, "You have been moved on to the next round as the odd player out.")
    }

    fun removeFromCombatPairs(player: Player, death: Boolean = false) {
        val state = state as? TournamentState.RoundActive ?: return
        player.interfaceHandler.closeInterface(InterfacePosition.WILDERNESS_OVERLAY)
        if (state.removeUnpairedPlayerIfEquals(player))
            return

        val pairs = state.combatPairs
        if (pairs.any { it.isSpectating(player) })
            return
        val pair = pairs.find(player)
        if (pair == null) {
            player.sendDeveloperMessage("No pair found for you, which should not happen! (death=$death)")
            logger.warn("No pair found for player {}", player)
            return
        }
        val opponent = pair.getOther(player)
        if (opponent == null) {
            player.sendDeveloperMessage("No opponent found for you, which should not happen! (death=$death)")
            logger.warn("No opponent found for player {}", player)
            return
        }
        logger.info("Removing pair {}", pair)
        pair.cancelSpectators(lobby)
        val remainingPlayers = countPlayersParticipating() - 2
        val placement = remainingPlayers + 2
        if (death) {
            player.sendTournamentMessage(buildString {
                append("You have been eliminated from the tournament in round ")
                append(state.round)
                append(" with ")
                append(remainingPlayers)
                append(" participant")
                append((if (remainingPlayers == 1) "" else "s"))
                append(" remaining.")
            })
            awardPointsForPlacement(player, placement = placement)
        }
        if (remainingPlayers >= 1)
            moveToNextRound(opponent, "You have been moved on to the next round.")
        else
            win(opponent)
        pairs.remove(pair)
    }

    private fun moveToNextRound(player: Player, message: String) {
        if (!lobby.inside(player.location))
            player.setLocation(lobby.randomFightingWaitAreaLocation)
        if(preset == TournamentPreset.MYSTERY_BOX || preset == TournamentPreset.F2P_PURE) {
            preset.apply(player)
            player.packetDispatcher.sendUpdateItemContainer(player.inventoryTemp.container)
            player.packetDispatcher.sendUpdateItemContainer(player.equipmentTemp.container)
        }
        player.restoreStatePostFight()
        player.lock(1)
        player.sendTournamentMessage(message)
    }

    internal fun win(winner: Player) {
        val previousState = state
        state = TournamentState.Finished.WonBy(winner)
        if (previousState is TournamentState.RoundActive) {
            previousState.fightArea.apply {
                players.toSet().forEach(lobby::teleportPlayer)
                destroyRegion()
            }
        }
        logger.info("Tournament has ended with a winner: {}", winner)
        val winnerLocation = lobby.getLocation(TournamentLobbyArea.WINNER_LOCATION)
        winner.interfaceHandler.closeInterface(InterfacePosition.MINIGAME_OVERLAY)
        winner.setLocation(winnerLocation)
        winner.sendTournamentMessage("Congratulations, you won the tournament!")
        awardPointsForPlacement(winner, 1)
        sendWinnerBroadcast(winner, preset)
        sendWinnerSpecialEffectGraphics(winnerLocation)
        if(preset == TournamentPreset.BOXING)
            awardGildedGloves(winner)
        for (player in lobby.players)
            player.interfaceHandler.closeInterface(InterfacePosition.MINIGAME_OVERLAY)
    }

    private fun awardGildedGloves(player: Player) {
        player.sendMessage("You have been awarded a set of Gilded Boxing Gloves for your win.")
        if(!player.previousBoxingWinner)
            player.sendMessage("In future boxing tournaments, you will always wear them.")
        if(player.gameMode == GameMode.ULTIMATE_IRON_MAN) {
            player.sendMessage("Please contact a manager to issue your gloves.")
            player.log(LogLevel.INFO, "Award UIM gloves")
        }
        player.bank.add(Item(CustomItemId.GILDED_BOXING_GLOVES))
        player.previousBoxingWinner = true
    }

    override fun toString(): String = buildString {
        append(Colour.RS_PURPLE.wrap(preset.toString()))
        append(" tournament ")
        val duration = Clock.System.now() - scheduledTime
        append(when(state) {
            is TournamentState.Scheduled -> "starts in"
            is TournamentState.Ongoing -> "started"
            is TournamentState.Finished -> "ended"
        })
        append(" "+ Colour.RS_PURPLE.wrap(duration.absoluteValue.formattedString))
    }

    fun currentRound(): Int = when(val state = state) {
        is TournamentState.Scheduled -> 1
        is TournamentState.Ongoing -> state.round
        is TournamentState.Finished -> 0
    }

    fun countPlayersParticipating() = when(val state = state) {
        is TournamentState.RoundActive -> participants.size + state.playersInCombat.size
        is TournamentState.Finished -> 0
        else -> participants.size
    }

    fun countPlayersFighting(): Int = when(val state = state) {
        is TournamentState.RoundActive -> state.playersInCombat.size
        else -> 0
    }

    fun cancel(reason: String) {
        fun cancelPlayer(player: Player) {
            player.sendTournamentMessage("The tournament has been cancelled $reason.")
            player.moveToTournamentPortal()
        }
        when(val state = state) {
            is TournamentState.Finished -> {
                logger.info("Could not cancel tournament as it has already finished.")
                return
            }
            is TournamentState.RoundActive -> {
                val fightArea = state.fightArea
                fightArea.players.toSet().forEach(::cancelPlayer)
                fightArea.destroyRegion()
            }
            else -> Unit
        }
        lobby.players.toSet().forEach(::cancelPlayer)
        logger.info("Tournament has been cancelled $reason.")
        state = TournamentState.Finished.NoWinner
    }

    fun endRound(expired: Boolean) {
        val state = state as? TournamentState.RoundActive ?: return
        val fightArea = state.fightArea
        logger.warn("Round {} ended (expired={})", state.round, expired)
        fightArea.players.toSet().forEach {
            moveToNextRound(it,
                if(expired) "You have been moved on to the next round as the round has expired."
                else "You have been moved on to the next round as your opponent has left."
            )
        }
        fightArea.destroyRegion()
        this.state = TournamentState.RoundOver(state.round + 1)
    }
}


private fun sendWinnerBroadcast(winner: Player, preset: TournamentPreset): Unit =
    World.sendMessage(MessageType.GLOBAL_BROADCAST, "${winner.name} has won the 1v1 $preset tournament!")

private fun sendWinnerSpecialEffectGraphics(tile: Location): Boolean =
    schedule({
        for (x in -1..1) {
            for (y in -1..1) {
                World.sendGraphics(Graphics(1388, Utils.random(60), 90), tile.transform(x, y, 0))
            }
        }
    })

private fun advanceChallengeProgress(player: Player): Unit =
    AdventCalendarManager.increaseChallengeProgress(player, 2022, 11, 1)

private fun awardPointsForPlacement(player: Player, placement: Int) {
    val points = when (placement) {
        1 -> 10
        2 -> 5
        3 -> 3
        in 4..10 -> 2
        in 10..20 -> 1
        else -> 0
    }
    ShopCurrencyHandler.add(ShopCurrency.TOURNAMENT_POINTS, player, points)
    player.sendTournamentMessage(
        buildString {
            append("You've been awarded ")
            append(Colour.RED.wrap(points))
            append(" points for finishing ")
            append(Utils.suffixOrdinal(placement))
            append(" place.")
        }
    )
}
