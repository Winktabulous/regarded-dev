package com.near_reality.game.content.araxxor.cave_hunt

import com.near_reality.game.content.*
import com.zenyte.game.item.ItemId.ARANEA_BOOTS
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World.spawnObject
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.plugins.dialogue.PlainChat

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-14
 */
data class AraxyteCaveHunt(
    val player: Player?
) : PrebuiltDynamicArea(allocatedArea = MapBuilder.findEmptyChunk(8, 8), region = 15002) {

    override fun name(): String = "araxyte_cave_hunt"

    private val objectType: Int = 10
    private val npcTile: Location = getLocation(3734, 9865, 0)
    private val exitLocation: Location = Location(3682, 9802, 0)

    private val northWebTunnelLocation: Location = Location(3731, 9868, 0)
    private val southWebTunnelLocation: Location = Location(3731, 9859, 0)
    private val westWebTunnelLocation: Location = Location(3727, 9863, 0)
    private val eastWebTunnelLocation: Location = Location(3736, 9863, 0)

    private var progression: Int = 0

    var roomCompleted: Boolean = true

    val entryTile: Location = getLocation(3731, 9865, 0)
    val correctTunnel: Int = WEB_TUNNEL_54155
    val wrongTunnel: Int = WEB_TUNNEL_54159

    override fun enter(player: Player?) {}

    fun progressCaveHunt() {
        progression++
        setupStage()
    }

    override fun constructed() {
        transferPlayer()
    }

    private fun setupStage() {
        if (progression < 8) {
            if (!passesPassiveCheck()) {
                player?.dialogue { plain("You get lost and find yourself back at the beginning...") }
                player?.setLocation(exitLocation)
                return
            }
        }
        npcs.forEach { it.remove() }
        when (progression) {
            0 -> spawnWithSafeToThe(North)
            1 -> spawnWithSafeToThe(North)
            2 -> spawnWithSafeToThe(South)
            3 -> spawnWithSafeToThe(South)
            4 -> spawnWithSafeToThe(West)
            5 -> spawnWithSafeToThe(East)
            6 -> spawnWithSafeToThe(West)
            7 -> spawnWithSafeToThe(East)
            8 -> {
                // Slay the Araxyte with Halbread
                spawnWithSafeToThe(WestEast)
                // spawn NPC
                val loneAraxyte = LoneAraxyte(this, npcTile)
                    loneAraxyte.radius = 0
                    loneAraxyte.spawn()
                addNpc(loneAraxyte)
            }
            9 -> {
                // Talk to Vefari with Slayer helm
                spawnWithSafeToThe(WestEast)
                val vefari = NPC(VEFARI, npcTile, true)
                    vefari.radius = 0
                    vefari.spawn()
                addNpc(vefari)
            }
            10 -> {
                // Talk to Araxi with Pet
                spawnWithSafeToThe(WestEast)
                val arancini = NPC(ARANCINI, npcTile, true)
                    arancini.radius = 0
                    arancini.spawn()
                addNpc(arancini)
            }
            11 -> {
                // use the amulet on the Man
                spawnWithSafeToThe(WestEast)
                val man = NPC(MAN_13679, npcTile, true)
                    man.radius = 0
                    man.spawn()
                addNpc(man)
            }
            12 -> {
                player?.setLocation(exitLocation)
                player?.mapInstance = null
                destroyRegion()
            }
        }
    }

    private fun spawnWithSafeToThe(dir: Direction) {
        when (dir) {
            North -> {
                spawnObject(WorldObject(correctTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            South -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            East -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            West -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            else -> {
                roomCompleted = false
                spawnObject(WorldObject(-1, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(-1, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
        }
    }

    private fun passesPassiveCheck(): Boolean {
        if (player == null) return false
        if (player.isDeveloper) return true
        val hasBoots = player.equipment.containsItem(ARANEA_BOOTS)
        val isEnvenomed = player.toxins.isVenomed
        return hasBoots && isEnvenomed
    }

    private fun transferPlayer() {
        if (player == null) return
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entryTile)
            player.faceDirection(Direction.WEST)
        }
        player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            if (!passesPassiveCheck()) {
                player.dialogue { plain("You get lost and find yourself back at the beginning...", false) }
                player.setLocation(exitLocation)
                player.unlock()
                return@schedule
            }
            setupStage()
            player.mapInstance = this@AraxyteCaveHunt
            player.unlock()
        }
    }
}