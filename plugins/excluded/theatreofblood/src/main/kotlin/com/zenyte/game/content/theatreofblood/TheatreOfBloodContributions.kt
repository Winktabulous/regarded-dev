package com.zenyte.game.content.theatreofblood

import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import com.zenyte.game.content.theatreofblood.room.nylocas.NylocasRoom
import com.zenyte.game.content.theatreofblood.room.pestilentbloat.PestilentBloatRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.SotetsegRoom
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.content.theatreofblood.room.xarpus.XarpusRoom
import com.zenyte.utils.TextUtils
import it.unimi.dsi.fastutil.longs.Long2IntMap

/**
 * THe initial amount of contribution points each player starts off with,
 */
const val START_CONTRIBUTION_POINTS = 8

/**
 * The number of contribution points subtracted when a player dies.
 */
const val DEATH_CONTRIBUTION_POINTS_PENALTY = 4

/**
 * The number of contribution points rewarded to the player for participating in an encounter.
 */
const val ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_REWARD = 3

private const val MAIDEN_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 2
private const val BLOAT_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 2
private const val NYLOCAS_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 1
private const val SOTETSEG_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 1
private const val XARPUS_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 2

/**
 * Awards contribution points to the player who dealt the most damage to the boss.
 *
 * @see [awardMostDamageContributionPointsToMVPForPhase] Verzik Vitur MVP point award handling
 */
internal fun TheatreRoom.awardMostDamageContributionPointsToMVP(){
    awardContributionPointsToMVP(damageMap, when(this) {
        is MaidenOfSugadintiRoom -> MAIDEN_MOST_DMG_CONTRIBUTION_POINTS_REWARD
        is PestilentBloatRoom -> BLOAT_MOST_DMG_CONTRIBUTION_POINTS_REWARD
        is NylocasRoom -> NYLOCAS_MOST_DMG_CONTRIBUTION_POINTS_REWARD
        is SotetsegRoom -> SOTETSEG_MOST_DMG_CONTRIBUTION_POINTS_REWARD
        is XarpusRoom -> XARPUS_MOST_DMG_CONTRIBUTION_POINTS_REWARD
        else -> return
    })
}

private const val VERZIK_PHASE_MOST_DMG_CONTRIBUTION_POINTS_REWARD = 2

/**
 * Awards contribution points to the player who dealt the most damage,
 * at every phase of Verzik Vitur.
 */
internal fun VerzikViturRoom.awardMostDamageContributionPointsToMVPForPhase() {
    awardContributionPointsToMVP(phaseDamageMap, VERZIK_PHASE_MOST_DMG_CONTRIBUTION_POINTS_REWARD)
    phaseDamageMap.clear()
}

private fun awardContributionPointsToMVP(damageMap: Long2IntMap, pointsAwarded: Int) {
    val mvp = damageMap.long2IntEntrySet()
        .maxByOrNull { it.intValue }
        ?.let { RaidingParty.getPlayer(TextUtils.longToName(it.longKey)) }
        ?:return
    mvp.sendDeveloperMessage("You are the MVP and are awarded $pointsAwarded contribution points")
    mvp.theatreContributionPoints += pointsAwarded
}

private const val POST_VERZIK_CONTRIBUTION_POINTS_SUBTRACTED = 8

/**
 * After Verzik Viture is killed, the [START_CONTRIBUTION_POINTS] awarded to every player at the start,
 * is then subtracted from each player.
 */
internal fun VerzikViturRoom.subtractContributionPointsAfterVerzik(){
    players.forEach { it.theatreContributionPoints -= POST_VERZIK_CONTRIBUTION_POINTS_SUBTRACTED }
}

/**
 * Returns the sum of [theatreContributionPoints] for each [player in the party][RaidingParty.players].
 */
internal fun RaidingParty.totalContributionPoints() =
    players.sumOf { it.theatreContributionPoints }

private const val MAX_ENCOUNTERS = 6
private const val MAX_ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_PER_PLAYER =
    MAX_ENCOUNTERS * ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_REWARD

private const val VERZIK_PHASE_COUNT = 3
private const val MAX_VERZIK_PHASE_MOST_DMG_CONTRIBUTION_POINTS =
    VERZIK_PHASE_COUNT * VERZIK_PHASE_MOST_DMG_CONTRIBUTION_POINTS_REWARD

/**
 * Returns the maximum achievable contribution points for this [RaidingParty].
 */
internal fun RaidingParty.maxContributionPoints() =
    maxContributionPoints(players.size)

/**
 * Returns the maximum achievable contribution for a part of [playerCount] players.
 */
internal fun maxContributionPoints(playerCount: Int): Int {
    var maxPoints = 0
    maxPoints += (playerCount * MAX_ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_PER_PLAYER)
    maxPoints += MAIDEN_MOST_DMG_CONTRIBUTION_POINTS_REWARD
    maxPoints += BLOAT_MOST_DMG_CONTRIBUTION_POINTS_REWARD
    maxPoints += NYLOCAS_MOST_DMG_CONTRIBUTION_POINTS_REWARD
    maxPoints += SOTETSEG_MOST_DMG_CONTRIBUTION_POINTS_REWARD
    maxPoints += XARPUS_MOST_DMG_CONTRIBUTION_POINTS_REWARD
    maxPoints += MAX_VERZIK_PHASE_MOST_DMG_CONTRIBUTION_POINTS
    return maxPoints
}
