package com.near_reality.game.content.boss.nex

import com.near_reality.game.world.entity.player.sendDeath
import com.zenyte.game.content.ItemRetrievalService
import com.zenyte.game.content.ItemRetrievalService.RetrievalServiceType
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin
import com.zenyte.game.world.region.area.plugins.DeathPlugin
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin

/**
 * !IMPORTANT! If you refactor this class make sure to also modify [com.zenyte.game.content.godwars.objects.GodwarsBossDoorObject] as it loads it through reflection.
 *
 * @author Stan van der Bend
 */
class AncientChamberArea : GodwarsDungeonArea(), LootBroadcastPlugin, CycleProcessPlugin, DeathPlugin {

    override fun enter(player: Player) {
        super.enter(player)
        player.viewDistance = Player.LARGE_VIEWPORT_RADIUS
    }

    override fun leave(player: Player, logout: Boolean) {
        super.leave(player, logout)
        if (!logout) {
            player.resetViewDistance()
        }
    }

    override fun isRaidArea(): Boolean {
        return true
    }

    override fun process() {
        players.forEach {
            it.sendAncientPortalVarbit()
            if (it.nexChokesCount > 0) {
                if (it.variables.ticksInterval % 2 == 0) {
                    for (other in players) {
                        if (other != it && !other.isDead && !other.isFinished) {
                            if (other.nexChokesCount <= 0) {
                                if (other.location.withinDistance(it.location, 1)) {
                                    if (COMBAT_AREA.contains(other)) {
                                        other.startChoking(true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun sendDeath(player: Player, source: Entity?): Boolean {
        return if (COMBAT_AREA.contains(player)) {
            NexModule.statistics.globalDeathCount++
            player.nexDeathCount++
            player.sendDeath {
                player.deathMechanics.service(RetrievalServiceType.ANCIENT_PRISON, source, true)
                player.sendMessage("A magical chest has retrieved some of your items. You can collect them from it in the Ancient Prison.")
                ItemRetrievalService.updateVarps(player)
            }
            if (NexModule.isNexSpawned()) {
                NexModule.nex!!.playerDied()
            }
            true
        } else
            false
    }

    override fun isSafe(): Boolean = false

    override fun getDeathInformation(): String? = null

    override fun getRespawnLocation(): Location? = null

    override fun polygons(): Array<RSPolygon> =
        arrayOf(COMBAT_AREA, SAFE_BANK_AREA)

    override fun name(): String =
        "Godwars Dungeon: Ancient Chamber"

    override fun chamberArea(): RSPolygon =
        COMBAT_AREA

    companion object {

        val COMBAT_AREA = RSPolygon(
            arrayOf(
                intArrayOf(2910, 5188),
                intArrayOf(2910, 5218),
                intArrayOf(2940, 5218),
                intArrayOf(2940, 5188)
            ), 0
        )
        val SAFE_BANK_AREA = RSPolygon(
            arrayOf(
                intArrayOf(2900, 5198),
                intArrayOf(2900, 5209),
                intArrayOf(2909, 5209),
                intArrayOf(2909, 5198)
            ), 0
        )

        private val instance: AncientChamberArea
            get() = GlobalAreaManager.getArea(AncientChamberArea::class.java)

        fun countPlayersInPrison(): Int {
            val area = instance
            return area.players.count { area.chamberArea().contains(it) }
        }

        fun getPlayers(): MutableSet<Player> = instance.players
    }
}
