package com.zenyte.game.content.theatreofblood.room.verzikvitur

import com.zenyte.cores.CoresManager
import com.zenyte.game.content.theatreofblood.*
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.room.*
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur.Companion.TRANSFORM_INTO_SECOND_PHASE_ID
import com.zenyte.game.content.theatreofblood.room.verzikvitur.first.SupportingPillar
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.utils.TimeUnit
import it.unimi.dsi.fastutil.longs.Long2IntMap
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap

/**
 * @author Jire
 */
internal class VerzikViturRoom(raid: TheatreOfBloodRaid, area: AllocatedArea, room: TheatreRoomType) :
    TheatreRoom(raid, area, room), FullMovementPlugin {

    val verzikVitur = VerzikVitur(this)

    val supportingPillars = arrayOfNulls<SupportingPillar>(6)

    /**
     * Tracks the damage per phase of Verzik Vitur, resets at the start of each phase.
     */
    val phaseDamageMap: Long2IntMap = Long2IntOpenHashMap(5)

    override val entranceLocation: Location = getLocation(3168, 4298, 0)
    override val vyreOrator: WorldObject? = null
    override val spectatingLocation: Location = getLocation(3168, 4313, 0)
    override var boss: TheatreBossNPC<out TheatreRoom>? = verzikVitur

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = false

    override val healthBarType get() = run {
        if (verzikVitur.id == TRANSFORM_INTO_SECOND_PHASE_ID) HealthBarType.REGULAR else when (verzikVitur.phase) {
            VerzikViturPhase.FIRST -> HealthBarType.CYAN
            VerzikViturPhase.SECOND, VerzikViturPhase.THIRD -> HealthBarType.REGULAR
            else -> HealthBarType.REMOVED
        }
    }

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.REWARDS

    override fun onLoad() {
        verzikVitur.spawn()

        var idx = 0
        fun pillar(x: Int, y: Int) {
            val pillar = SupportingPillar(this, getBaseLocation(x, y))
            supportingPillars[idx++] = pillar
            pillar.spawn()
        }

        WorldTasksManager.schedule {
            for (y in 18..30 step 6) {
                pillar(25, y)
                pillar(37, y)
            }
        }
    }

    override fun onComplete() {
        super.onComplete()

        raid.enterTick = WorldThread.WORLD_CYCLE - raid.enterTick
        raid.completed = true

        if(!raid.bypassMode) {
            val trioChaser =
                raid.party.size == 3 && TimeUnit.TICKS.toMinutes(raid.totalRoomTicks) < 22//Original 20, we making it 22
            val scale4Chaser =
                raid.party.size == 4 && TimeUnit.TICKS.toMinutes(raid.totalRoomTicks) < 20//Original 17, we making it 20
            val scale5Chaser =
                raid.party.size == 5 && TimeUnit.TICKS.toMinutes(raid.totalRoomTicks) < 18//Original 16, we making it 18

            var scytheEquipped = false
            for (m in raid.party.members) {
                val p = RaidingParty.getPlayer(m) ?: continue

                p.slayer.checkAssignment(verzikVitur)
                p.varManager.sendBit(THRONE_VARBIT, 0) // ensure it's hidden
                if (raid.hardMode) {
                    if (!p.hasPrivilege(PlayerPrivilege.ADMINISTRATOR)) {
                        p.tobStatsHard.complete(p, raid.party.size, raid.totalRoomTicks, raid.enterTick)
                        p.sendMessage("Your completed Theatre of Blood count is: " + Colour.RED.wrap(p.tobStatsHard.completions) + ".")
                    } else
                        p.sendMessage("Your completion has not been recorded as you are an administrator.")
                } else {

                    if (!p.hasPrivilege(PlayerPrivilege.ADMINISTRATOR)) {
                        p.tobStats.complete(p, raid.party.size, raid.totalRoomTicks, raid.enterTick)
                        p.sendMessage("Your completed Theatre of Blood count is: " + Colour.RED.wrap(p.tobStats.completions) + ".")
                    } else
                        p.sendMessage("Your completion has not been recorded as you are an administrator.")
                    if (trioChaser) {//Original 20, we making it 22
                        p.combatAchievements.complete(CAType.THEATRE__TRIO__SPEED_CHASER)
                    }
                    if (scale4Chaser) {
                        p.combatAchievements.complete(CAType.THEATRE__4_SCALE__SPEED_CHASER)
                    }
                    if (scale5Chaser) {
                        p.combatAchievements.complete(CAType.THEATRE__5_SCALE__SPEED_CHASER)
                    }
                    if (p.tobStats.completions >= 25) {
                        p.combatAchievements.complete(CAType.THEATRE_OF_BLOOD_VETERAN)
                    }
                    if (p.tobStats.completions >= 75) {
                        p.combatAchievements.complete(CAType.THEATRE_OF_BLOOD_MASTER)
                    }
                    if (p.tobStats.completions >= 150) {
                        p.combatAchievements.complete(CAType.THEATRE_OF_BLOOD_GRANDMASTER)
                    }
                }

                if (p.scytheEquipped) {
                    scytheEquipped = true
                }
            }

            if (!scytheEquipped) {
                for (m in raid.party.members) {
                    val p = RaidingParty.getPlayer(m) ?: continue
                    p.combatAchievements.complete(CAType.BACK_IN_MY_DAY)
                }
            }
        }

        VerSinhazaArea.statistics[if (raid.hardMode) 1 else 0].complete(null, raid.party.size, raid.totalRoomTicks, raid.enterTick)
        CoresManager.slowExecutor.execute(TheatreOfBloodScoresSerializer::write)

        val pos = getBaseLocation(31, 36)
        val throne = WorldObject(ObjectId.TREASURE_ROOM, tile = pos).apply {
            sizeX += 2
            sizeY += 2
        }
        World.spawnObject(throne)
    }

    override fun onStart(player: Player) {
        verzikVitur.switchPhase(VerzikViturPhase.FIRST)
    }

    override fun onTheatreEnter(player: Player) {
        super.onTheatreEnter(player)

        player.varManager.sendBit(THRONE_VARBIT, 0) // ensure it hidden

        player.lock(5)
        WorldTasksManager.schedule {
            if (entered.add(player)) {
                player.setRunSilent(4)
                player.addWalkSteps(Direction.NORTH, 5, -1, false)

                player.sendMessage("Welcome to the final fight.")
                if(raid.bypassMode) {
                    player.sendMessage(Colour.RS_RED.wrap("You have been granted the Dawnbringer."))
                    player.inventory.addOrDrop(ItemId.DAWNBRINGER)
                }
            }
        }
    }

    /**
     * In addition to the overall [damageMap] we also update the [phaseDamageMap],
     * which resets every phase, used in order to track the MVP (player who dealt most damage) per phase.
     */
    override fun playerDealtDamage(player: Player, damage: Int) {
        super.playerDealtDamage(player, damage)

        val mapKey = damageMapKey(player)
        val totalDamage = phaseDamageMap.getOrDefault(mapKey, 0)
        phaseDamageMap.put(mapKey, totalDamage + damage)
    }

    override val jailLocations = Companion.jailLocations

    companion object {

        const val THRONE_VARBIT = 6400

        val jailLocations = arrayOf(
            JailLocation(21, 37),
            JailLocation(23, 37),
            JailLocation(25, 37),
            JailLocation(39, 37),
            JailLocation(41, 37)
        )

    }

    override fun processMovement(player: Player, x: Int, y: Int): Boolean {
        if (verzikVitur.webLocs.contains(player.location)) {
            if (player.araneaBoots.isPlayerWebImmune())
                return true
            player.resetWalkSteps()
            player.sendMessage("You're stuck in the web!")
            player.freeze(10)
            return false
        }

        return true
    }

}
