package com.zenyte.game.content.theatreofblood.room

import com.near_reality.game.world.spawnFloorItem
import com.zenyte.cores.CoresManager
import com.zenyte.game.GameInterface
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.content.theatreofblood.*
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.interfaces.TheatreOfBloodSuppliesInterface.Companion.tobPoints
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.RSColour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.area.plugins.*
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.plugins.dialogue.PlainChat
import com.zenyte.utils.TextUtils
import it.unimi.dsi.fastutil.longs.Long2IntMap
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import mgi.utilities.StringFormatUtil

/**
 * @author Jire
 * @author Tommeh
 */
internal abstract class TheatreRoom(
    val raid: TheatreOfBloodRaid,
    area: AllocatedArea,
    val roomType: TheatreRoomType
) : DynamicArea(area, roomType.chunkX, roomType.chunkY),
    CycleProcessPlugin, DeathPlugin, CannonRestrictionPlugin, LogoutPlugin, LogoutRestrictionPlugin, HitProcessPlugin, TeleportPlugin {

    open fun onLoad() {}

    open fun onStart(player: Player) {}

    override fun onLogout(player: Player) {
        leave(player, true)
    }

    override fun isMultiwayArea(position: Position) = true

    abstract var nextRoomType: TheatreRoomType?

    abstract val healthBarType: HealthBarType

    fun refreshHealthBar() {
        val party = raid.party
        for (m in party.members) {
            val member = getPlayer(m) ?: continue
            refreshHealthBar(member)
        }
    }

    fun refreshHealthBar(player: Player) {
        if (player.area != raid.getActiveRoom()) return

        if (currentHitpoints == 0) {
            player.varManager.sendBit(6447, 0)
            return
        }

        player.varManager.sendBit(6447, healthBarType.id)
        player.varManager.sendBit(6448, currentHitpoints / 10)
        player.varManager.sendBit(6449, maximumHitpoints / 10)
    }

    override fun isRaidArea(): Boolean {
        return true
    }

    fun removeHealthBar(player: Player) = player.varManager.sendBit(6447, HealthBarType.REMOVED.id)

    val entered: MutableSet<Player> = HashSet(5)
    var started = false
    var startTick = -1L
    var completed = false
    var timeTook = -1L

    val damageMap: Long2IntMap = Long2IntOpenHashMap(5)

    abstract val entranceLocation: Location?
    abstract val vyreOrator: WorldObject?
    abstract val spectatingLocation: Location?
    abstract var boss: TheatreBossNPC<out TheatreRoom>?
    open var chestInfo: ChestInfo? = null

    val validTargets: List<Player>
        get() = entered.filter { isValidTarget(it) }

    fun isValidTarget(player: Player) =
        !player.isDead && !player.isFinished && raid.party.members.contains(player.username)
                && !player.temporaryAttributes.containsKey("tob_jailed")

    abstract fun isEnteringBossRoom(barrier: WorldObject, player: Player): Boolean

    open fun onTheatreEnter(player: Player) {}

    override fun constructed() {
        if (vyreOrator != null)
            World.spawnObject(vyreOrator)
    }

    override fun enter(player: Player) {
        player.viewDistance = Player.SCENE_DIAMETER
    }

    private fun dropDawnbringer(player: Player, location: Location) {
        val inventory = player.inventory
        for (i in 0..27) {
            val item = inventory.getItem(i) ?: continue
            val id = item.id
            if (id == ItemId.DAWNBRINGER) {
                World.spawnFloorItem(item, location, -1, player, player, -1, -1)
                inventory[i] = null
            }
        }
        inventory.refreshAll()

        val weapon = player.weapon?: return
        if (weapon.id == ItemId.DAWNBRINGER) {
            World.spawnFloorItem(weapon, location, -1, player, player, -1, -1)
            player.equipment.replaceItem(-1, 0, EquipmentSlot.WEAPON.slot)
        }
    }

    override fun leave(player: Player, logout: Boolean) {
        player.resetForLeave()

        val party = raid.party
        if (logout) {
            dropDawnbringer(player, player.location)
            player.resetViewDistance()
            player.attributes[TheatreOfBloodLoginSubscriber.LAST_TOB_PARTY_ID] = party.id
            player.forceLocation(Location(3372, 5155, 2))
            party.removeMember(player)
            return
        }

        val nextArea = GlobalAreaManager.getArea(player.location)
        if (nextArea !is TheatreRoom) {
            dropDawnbringer(player, Location(player.lastLocation))
            player.interfaceHandler.closeInterface(GameInterface.TOB_PARTY)
            removeHealthBar(player)
            party.removeMember(player)
        }
    }

    override fun manualLogout(player: Player): Boolean {
        player.dialogue {
            options("Are you sure you wish to log out?") {
                "Yes" {
                    player.logout(false)
                }
                "No"()
            }
        }
        return false
    }

    override fun onLoginLocation() = null

    fun start(player: Player) {
        if (started) return

        started = true
        startTick = WorldThread.WORLD_CYCLE

        /* Need to initialize the damage map, in case they didn't deal any damage, for later */
        for (i in 0..raid.party.members.lastIndex) {
            val username = raid.party.members[i]
            damageMap.put(damageMapKey(StringFormatUtil.formatUsername(username)), 0)
        }

        onStart(player)
    }

    open fun enterBossRoom(barrier: WorldObject, player: Player) {
        start(player)
        passBarrier(barrier, player)
        WorldTasksManager.schedule({
            refreshHealthBar(player)
            if (!entered.contains(player)) {
                entered.add(player)
                player.theatreContributionPoints += ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_REWARD
                player.tobPoints += ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_REWARD
            }
        }, 2)
    }

    fun handleBarrier(barrier: WorldObject, player: Player) {
        player.lock(2)
        player.setRunSilent(2)
        if (isEnteringBossRoom(barrier, player)) {
            if(raid.bypassMode) {
                player.dialogueManager.start(
                    PlainChat(
                        player,
                        "The Maiden directs you to the next chamber for your final performance."
                    )
                )
                return
            }
            if (!started) {
                if (!raid.party.isLeader(player)) {
                    player.dialogueManager.start(
                        PlainChat(
                            player,
                            "Your leader, " + raid.party.leader!!.name + ", must go first."
                        )
                    )
                    return
                }
                player.options("Is your party ready to fight?") {
                    "Yes, let's begin." { enterBossRoom(barrier, player) }
                    "No, don't start yet."()
                }
            } else enterBossRoom(barrier, player)
        } else {
            val boss = boss
            if (boss != null && !checkBarrier(player, boss.isFinished)) return
            passBarrier(barrier, player)
        }
    }

    fun handlePassage(player: Player) {
        val currentRoom = player.area as TheatreRoom
        if (!currentRoom.completed && !raid.bypassMode) {
            if (!player.privilege.eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                player.sendMessage("You can't proceed until the challenge is complete.")
                return
            } else {
                player.sendMessage("You cheat your way through the room. Completed=${currentRoom.completed}")
            }
        }
        if (raid.getActiveRoom() == currentRoom) {
            val nextRoomType = nextRoomType!!
            raid.removeRoom(roomType)
            raid.addRoom(nextRoomType)

            raid.advance(player, nextRoomType)
            raid.constructMap()
        }
        PartyOverlayInterface.fadeRedPortal(player, "")
        WorldTasksManager.schedule {
            val activeRoom = raid.getActiveRoom()!!
            PartyOverlayInterface.fade(player, 255, 0, activeRoom.roomType.roomName)
            val entranceLocation = activeRoom.entranceLocation!!
            player.setLocation(entranceLocation)
            player.addTemporaryAttribute("tob_advancing_room", 0)

            activeRoom.onTheatreEnter(player)
        }
    }

    fun complete() {
        if (!completed) {
            completed = true
            onComplete()
        }
    }

    open fun onComplete() {
        awardMostDamageContributionPointsToMVP()

        chestInfo?.spawnInArea(this)

        val entry = damageMap.long2IntEntrySet().maxByOrNull { it.intValue }
        if (entry != null) {
            val usernameBase37 = entry.longKey
            val username = TextUtils.longToName(usernameBase37)
            val player = getPlayer(username)
            if (player != null) {
                player.tobPoints += 3
            }
        }

        timeTook = WorldThread.WORLD_CYCLE - startTick
        raid.totalRoomTicks += timeTook

        val message = if (this is VerzikViturRoom) "Wave '${name()}' complete!" +
                " Duration: ${Colour.RED.wrap(Utils.ticksToTime(timeTook))}"
        else
                 "Wave '${name()}' complete!" +
                " Duration: ${Colour.RED.wrap(Utils.ticksToTime(timeTook))}" +
                " Total: ${Colour.RED.wrap(Utils.ticksToTime(raid.totalRoomTicks))}"

        for (memberName in raid.party.members) {
            val player = getPlayer(memberName) ?: continue
            removeHealthBar(player)
            player.packetDispatcher.playJingle(250)
            player.sendMessage(message)

            player.heal(player.maxHitpoints)
            player.prayerManager.restorePrayerPoints(99)
            player.combatDefinitions.specialEnergy = 100
            player.variables.runEnergy = 100.0

            restorePlayerFromJail(player, this)
        }
    }

    override fun canTeleport(player: Player, teleport: Teleport): Boolean {
        if (teleport == TeleportCollection.VERZIK_CRYSTAL_SHARD) {
            return true
        }
        player.dialogueManager.start(PlainChat(player, "You can't teleport from the Theatre of Blood."))
        return false
    }

    override fun hit(source: Player, target: Entity, hit: Hit, modifier: Float) = entered.contains(source)

    override fun process() {
        if(raid.party.players.size == 1) {
            val player = raid.party.players.stream().findAny().get()
            if(player.tobBypassHook) {
                player.tobBypassHook = false
                nextRoomType = TheatreRoomType.VERZIK_VITUR
                raid.bypassMode = true
                player.sendMessage(Colour.RS_RED.wrap("The Theatre recognizes your sacrifice and invites you to a special performance."))
                WorldTasksManager.schedule({ VerSinhazaArea.getArea(player)?.handlePassage(player) }, 1, -1)
            }
        }
    }

    override fun isSafe() = false

    override fun getDeathInformation() = null

    override fun getRespawnLocation() = entranceLocation

    override fun sendDeath(player: Player, source: Entity?): Boolean {
        if(completed) {
            player.animation = Animation.STOP
            player.tobDeathSpot = player.location.copy()
            player.lock()
            player.stopAll()
            player.packetDispatcher.resetCamera()

            WorldTasksManager.schedule(object : WorldTask {
                var ticks = 0

                override fun run() {
                    if (player.isFinished || player.isNulled || raid.completed || raid.failed) {
                        stop()
                        return
                    }
                    if (ticks == 1) {
                        player.animation = Player.DEATH_ANIMATION
                    } else if (ticks == 3) {
                        player.reset()
                        player.blockIncomingHits(5)
                        dropDawnbringer(player, player.location)
                        player.animation = Animation.STOP
                        if (player.variables.isSkulled)
                            player.variables.setSkull(false)
                    } else if (ticks == 4) {
                        player.unlock()
                        player.appearance.resetRenderAnimation()
                        player.animation = Animation.STOP
                        stop()
                    }
                    ticks++
                }
            }, 0, 1)
            return true

        } else {
            player.resetForLeave()

            if (raid.hardMode) {
                player.tobStatsHard.deaths++
            } else {
                player.tobStats.deaths++
            }
            VerSinhazaArea.statistics[if (raid.hardMode) 1 else 0].deaths++
            CoresManager.slowExecutor.execute(TheatreOfBloodScoresSerializer::write)

            player.animation = Animation.STOP
            player.tobDeathSpot = player.location.copy()
            player.lock()
            player.stopAll()
            player.packetDispatcher.resetCamera()
            player.theatreDeathCount++
            player.theatreContributionPoints -= DEATH_CONTRIBUTION_POINTS_PENALTY
            player.tobPoints -= ENCOUNTER_PARTICIPATION_CONTRIBUTION_POINTS_REWARD

            if (player.prayerManager.isActive(Prayer.RETRIBUTION))
                player.prayerManager.applyRetributionEffect(source)
            WorldTasksManager.schedule(object : WorldTask {
                var ticks = 0

                override fun run() {
                    if (player.isFinished || player.isNulled || raid.completed || raid.failed) {
                        stop()
                        return
                    }
                    if (ticks == 1) {
                        player.animation = Player.DEATH_ANIMATION
                    } else if (ticks == 2) {
                        PartyOverlayInterface.fade(player, 255, 0, "If your party survives the wave, you will respawn.")
                    } else if (ticks == 3) {
                        deathCount++

                        player.reset()
                        player.blockIncomingHits(5)
                        dropDawnbringer(player, player.location)
                        player.animation = Animation.STOP
                        if (player.variables.isSkulled)
                            player.variables.setSkull(false)

                        if (nextJailLocation()?.sendPlayer(player, raid) == true) {
                            stop()
                            return
                        }
                    } else if (ticks == 4) {
                        player.unlock()
                        player.appearance.resetRenderAnimation()
                        player.animation = Animation.STOP
                        stop()
                    }
                    ticks++
                }
            }, 0, 1)
            return true
        }
    }

    override fun postProcess() {
        val party = raid.party
        party.updateStatusHUD(false)
    }

    open val currentHitpoints: Int
        get() {
            val boss = boss
            return if (boss == null || boss.isFinished) 0
            else boss.hitpoints
        }

    open val maximumHitpoints: Int
        get() {
            val boss = boss
            return if (boss == null || boss.isFinished) 0
            else boss.maxHitpoints
        }

    fun Player.resetForLeave() {
        //isForceMultiArea = false
        entered.remove(this)
    }

    override fun dropOnGround(player: Player, item: Item): Boolean {

        if (item.id == ItemId.DAWNBRINGER) {
            spawnFloorItem(item, owner = player, visibleToIronmen = true)
            return false
        }

        return super.dropOnGround(player, item)
    }

    override fun visibleTicks(player: Player?, item: Item?): Int {
        return -1
    }

    override fun invisibleTicks(player: Player?, item: Item): Int {
        return if (item.id == ItemId.DAWNBRINGER) -1 else Int.MAX_VALUE
    }

    open fun playerDealtDamage(player: Player, damage: Int) {
        val mapKey = damageMapKey(player)
        val totalDamage = damageMap.getOrDefault(mapKey, 0)
        damageMap.put(mapKey, totalDamage + damage)

        player.theatreDamageDealt += damage
    }

    open val jailLocations: Array<JailLocation>? = null

    open fun nextJailLocation(): JailLocation? = jailLocations?.get(deathCount % jailLocations!!.size)

    var deathCount = 0

    override fun name() = roomType.roomName

    companion object {

        fun restorePlayerFromJail(player: Player, room: TheatreRoom) {
            if (JailLocation.restorePlayer(player)) {
                var respawnLocation = if (room is VerzikViturRoom)
                    player.tobDeathSpot
                else
                    player.tobPreBarrierSpot
                if (respawnLocation == null || respawnLocation.x == 0 || respawnLocation.y == 0)
                    respawnLocation = room.entranceLocation
                player.sendDeveloperMessage("Post jail location = $respawnLocation")
                player.setLocation(respawnLocation)
            }
        }

        internal fun damageMapKey(formattedUsername: String) = TextUtils.stringToLong(formattedUsername)

        internal fun damageMapKey(player: Player) = damageMapKey(StringFormatUtil.formatUsername(player.username))

        private fun checkBarrier(player: Player, check: Boolean): Boolean {
            if (!check) {
                val message = "You must stay and fight!"
                player.sendMessage(message)
                player.dialogueManager.start(NPCChat(player, NpcId.VYRE_ORATOR, message))
            }
            return check
        }

        private fun passBarrier(barrier: WorldObject, player: Player) {
            player.tobPreBarrierSpot = player.location.copy()
            player.lock(2)
            player.setRunSilent(2)
            if (barrier.rotation == 0 || barrier.rotation == 2)
                player.addWalkSteps(player.x, barrier.y + if (player.y > barrier.y) -1 else 1, -1, false)
            else
                player.addWalkSteps(barrier.x + if (player.x > barrier.x) -1 else 1, player.y, -1, false)
        }

    }

}
