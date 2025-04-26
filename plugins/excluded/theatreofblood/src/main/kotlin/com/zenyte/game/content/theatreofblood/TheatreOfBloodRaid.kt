package com.zenyte.game.content.theatreofblood

import com.zenyte.cores.CoresManager
import com.zenyte.game.content.ItemRetrievalService
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.interfaces.TheatreOfBloodSuppliesInterface.Companion.tobPoints
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.content.theatreofblood.room.JailLocation
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.room.TheatreRoomType
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.LoggerFactory

/**
 * @author Jire
 * @author Tommeh
 */
internal class TheatreOfBloodRaid(
    val party: RaidingParty,
    val hardMode: Boolean = party.hardMode
) {

    var bypassMode: Boolean = false
    private val typeToRoom: Object2ObjectMap<TheatreRoomType, TheatreRoom> =
        Object2ObjectOpenHashMap(TheatreRoomType.values.size)

    val spectators: ObjectSet<String> = ObjectOpenHashSet()

    var completed = false
    var failed = false

    var enterTick = -1L
    var totalRoomTicks = 0L

    fun onLogin(player: Player) {
        val room = getActiveRoom() ?: return
        player.setLocation(room.entranceLocation)
        party.members.add(player.username)

        for (m in party.members) {
            val member = getPlayer(m) ?: continue
            party.initializeStatusHUD(member)
        }
        PartyOverlayInterface.fade(player, 255, 0, "You have rejoined your party.")
    }

    fun enter(p: Player) {
        p.lock(4)
        if (party.isLeader(p)) {
            val roomType = initialRoomType
            addRoom(roomType)
            advance(p, roomType)
            constructMap()
            enterTick = WorldThread.WORLD_CYCLE
        }
        val originalMembers = party.originalMembers
        if (!originalMembers.contains(p.username))
            originalMembers.add(p.username)
        p.sendSound(SoundEffect(1952))
        p.packetDispatcher.sendClientScript(2379)
        p.tobPlayerCount = originalMembers.size
        p.pendingTOBBypass = false
        p.tobPoints = 0
        p.theatreDeathCount = 0
        p.theatreDamageDealt = 0
        p.theatreContributionPoints = START_CONTRIBUTION_POINTS
        p.insideTob = true
        p.temporaryAttributes.remove("tob_jailed")
        checkForScythe(p)
        PartyOverlayInterface.fade(p, 0, 0, "The Theatre awaits...")
        WorldTasksManager.schedule({
            PartyOverlayInterface.fade(p, 255, 0, getActiveRoom()!!.roomType.roomName)
            party.initializeStatusHUD(p)
            val activeRoom = getActiveRoom()!!
            p.setLocation(activeRoom.entranceLocation)
            activeRoom.onTheatreEnter(p)
            p.addTemporaryAttribute("tob_advancing_room", 0)
        }, 2)

        if (hardMode) {
            p.tobStatsHard.attempts++
        } else {
            p.tobStats.attempts++
        }
        VerSinhazaArea.statistics[if (hardMode) 1 else 0].attempts++
        CoresManager.slowExecutor.execute(TheatreOfBloodScoresSerializer::write)
    }

    private fun checkForScythe(player: Player) {
        when (player.equipment.getId(EquipmentSlot.WEAPON)) {
            ItemId.SCYTHE_OF_VITUR,
            ItemId.SCYTHE_OF_VITUR_UNCHARGED,
            ItemId.HOLY_SCYTHE_OF_VITUR,
            ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED,
            ItemId.SANGUINE_SCYTHE_OF_VITUR,
            ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED -> player.scytheEquipped = true
        }
    }

    fun advance(player: Player, roomType: TheatreRoomType) {
        for (m in party.members) {
            val member = getPlayer(m) ?: continue
            if (member == player) continue
            if (roomType == initialRoomType) {
                member.varManager.sendBit(6440, 2)
                member.sendMessage(player.name + " has entered the Theatre of Blood. Step inside to join ${player.himHer}...")
            } else {
                val activeRoom = getActiveRoom()
                if (activeRoom != null) {
                   TheatreRoom.restorePlayerFromJail(member, activeRoom)
                }
                member.sendMessage(player.name + " has advanced to Wave " + roomType.wave + ". Join ${player.himHer}...")
            }
        }
        player.addTemporaryAttribute("tob_advancing_room", 1)
        getActiveRoom()!!.onLoad()
    }

    fun activityFailed() {
        failed = true
        for (m in party.members) {
            val member = getPlayer(m) ?: continue
            leave(member, "Your party has failed.", true)
            member.deathMechanics.checkHardcoreIronManDeath()
            member.deathMechanics.service(ItemRetrievalService.RetrievalServiceType.THEATRE_OF_BLOOD, null, false)
            member.sendMessage("A magical chest has retrieved some of your items. You can collect them from it outside of the Theatre of Blood.")
            ItemRetrievalService.updateVarps(member)
        }
    }

    fun leave(player: Player, text: String, heal: Boolean) {
        PartyOverlayInterface.fadeRedPortal(player, text)
        WorldTasksManager.schedule({
            party.removeMember(player)
            player.insideTob = false
            player.setLocation(outsideLocation)
            PartyOverlayInterface.fade(player, 200, 0, text)
            JailLocation.restorePlayerInner(player)
            if (heal) {
                player.heal(player.maxHitpoints)
            }
        }, 2)
    }

    fun getRoom(roomType: TheatreRoomType) = typeToRoom[roomType]

    fun getRoomTime(roomType: TheatreRoomType): String {
        val room = getRoom(roomType) ?: return "-"
        return Utils.ticksToTime(room.timeTook)
    }

    fun getActiveRoom(): TheatreRoom? {
        if (typeToRoom.isEmpty()) return null

        var area: TheatreRoom? = null
        var wave = 0
        for (entry in typeToRoom.entries) {
            val room = entry.key
            val roomWave = room.wave
            if (roomWave > wave) {
                wave = roomWave
                area = entry.value
            }
        }
        return area
    }

    fun constructMap() {
        for (area in typeToRoom.values)
            if (!area.completed)
                area.constructRegion()
    }

    fun addRoom(roomType: TheatreRoomType) {
        try {
            val allocatedArea = MapBuilder.findEmptyChunk(roomType.sizeX, roomType.sizeY)
            val area = roomType.roomClass.java.getDeclaredConstructor(
                TheatreOfBloodRaid::class.java,
                AllocatedArea::class.java,
                TheatreRoomType::class.java
            ).newInstance(this@TheatreOfBloodRaid, allocatedArea, roomType)
            typeToRoom[roomType] = area
        } catch (e: Exception) {
            logger.error("", e)
        }
    }

    fun removeRoom(roomType: TheatreRoomType) {
        val area = typeToRoom[roomType] ?: return
        area.destroyRegion()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(TheatreOfBloodRaid::class.java)

        val outsideLocation = Location(3677, 3217)

        val initialRoomType: TheatreRoomType = TheatreRoomType.THE_MAIDEN_OF_SUGADINTI

        private val Player.himHer get() = if (appearance.isMale) "him" else "her"

    }

}
