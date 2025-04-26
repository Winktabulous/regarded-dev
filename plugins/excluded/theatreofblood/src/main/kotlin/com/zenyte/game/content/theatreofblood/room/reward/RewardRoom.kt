package com.zenyte.game.content.theatreofblood.room.reward

import com.zenyte.game.content.theatreofblood.*
import com.zenyte.game.content.theatreofblood.room.HealthBarType
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.room.TheatreRoomType
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Jire
 */
internal class RewardRoom(raid: TheatreOfBloodRaid, area: AllocatedArea, room: TheatreRoomType) :
    TheatreRoom(raid, area, room) {

    private val log = LoggerFactory.getLogger(RewardRoom::class.java)
    override val entranceLocation: Location = getBaseLocation(37, 19)
    override val vyreOrator: WorldObject? = null
    override val spectatingLocation: Location? = null
    override var boss: TheatreBossNPC<out TheatreRoom>? = null

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = false

    override val healthBarType = HealthBarType.REMOVED

    override var nextRoomType: TheatreRoomType? = null

    var playersLoot: MutableMap<Player, Container> = hashMapOf()

    override fun onLoad() {
        val party = raid.party
        val completeRequirement = completedInTime[0.coerceAtLeast(party.size - 1)]
        val completedInTime = raid.enterTick <= completeRequirement
        log.debug("Completed in time $completedInTime. Time ${Utils.ticksToTime(raid.enterTick)}, required ${Utils.ticksToTime(completeRequirement)}")
        var mode = if (completedInTime) TheatreOfBloodMode.HARD else TheatreOfBloodMode.NORMAL
        if(raid.bypassMode) {
            mode = TheatreOfBloodMode.BYPASS
        }
        log.debug("Rolling rewards for raid in mode {}", mode)
        val playerRewards = TheatreOfBloodRewardGenerator.roll(party, mode, completedInTime)
        log.debug("Total points for party = ${party.totalContributionPoints()}/${party.maxContributionPoints()}")
        for ((player, rewards) in playerRewards) {
            log.debug("Rewards for ${player.name}: (points = ${player.theatreContributionPoints})")
            val container = Container(ContainerPolicy.ALWAYS_STACK, ContainerType.THEATRE_OF_BLOOD, Optional.of(player))
            container.addAll(rewards)
            playersLoot[player] = container

            for (reward in rewards) {
                log.debug("\t ${reward.amount.toString().padEnd(10)} x ${reward.name.padEnd(40)} (${reward.id})")
            }

            val varbitId = chestVarbits[party.players.indexOf(player)]
            val varbitValue = if (checkForJackpot(container)) PURPLE_CHEST else REGULAR_CHEST
            for (partyPlayer in party.players) {
                val transformedVarbitValue = if (player == partyPlayer) if (varbitValue == PURPLE_CHEST) PURPLE_CHEST_ARROW else REGULAR_CHEST_ARROW else varbitValue
                partyPlayer.varManager.sendBit(varbitId, transformedVarbitValue)
            }
        }
        log.debug("---------------------------------------------")

        WorldTasksManager.schedule {
            val size = party.size
            generateChest(0)
            if (size > 1)
                generateChest(1)
            if (size > 2)
                generateChest(2)
            if (size > 3)
                generateChest(3)
            if (size > 4)
                generateChest(4)
        }
    }

    private fun generateChest(index: Int) {
        val coords = chestCoords[index]
        val x = coords[0]
        val y = coords[1]
        World.spawnObject(WorldObject(chestIds[index], 10, if (x == 40) 1 else if (x == 26) 3 else 0, getBaseLocation(x, y)))
    }

    companion object {
        val completedInTime = arrayOf(TimeUnit.MINUTES.toTicks(65), TimeUnit.MINUTES.toTicks(30), TimeUnit.MINUTES.toTicks(25), TimeUnit.MINUTES.toTicks(23), TimeUnit.MINUTES.toTicks(22))
        val chestCoords = arrayOf(intArrayOf(33, 42), intArrayOf(40, 39), intArrayOf(26, 39), intArrayOf(26, 35), intArrayOf(40, 35))
        val chestIds = arrayOf(33086, 33087, 33088, 33089, 33090)
        val chestVarbits = arrayOf(6450, 6451, 6452, 6453, 6454)
        const val REGULAR_CHEST = 0
        const val PURPLE_CHEST = 1
        const val REGULAR_CHEST_ARROW = 2
        const val PURPLE_CHEST_ARROW = 3

        fun checkForJackpot(items: Container): Boolean {
            if (items.isEmpty) {
                return false
            }

            return when (items[0].id) {
                22477, 22326, 22328, 22327, 22481, 22486, 22324 -> true
                else -> false
            }
        }
    }

}