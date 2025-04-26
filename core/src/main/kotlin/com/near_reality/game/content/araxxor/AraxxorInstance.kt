package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.araxxor.araxytes.AraxytePattern
import com.near_reality.game.content.araxxor.attacks.AcidPool
import com.near_reality.game.content.offset
import com.near_reality.game.content.seq
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId.ARAXXOR
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.plugins.dialogue.PlainChat
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
data class AraxxorInstance(
    val player: Player?
) : PrebuiltDynamicArea(allocatedArea = MapBuilder.findEmptyChunk(16, 16), region = 14489),
    CannonRestrictionPlugin {

    override fun name(): String = "Araxxor Instance"

    val centerArenaTile: Location = getLocation(3633, 9816, 0)
    private val entranceLocation: Location = getLocation(3647, 9815, 0)

    val araxxor: Araxxor = Araxxor(this, getLocation(3631, 9812, 0))

    val poolObjects = mutableListOf<AcidPool>()
    val araxytes = mutableListOf<Araxyte>()

    override fun constructed() {
        transferPlayer()
    }

    override fun leave(player: Player, logout: Boolean) {
        if(player.hpHud.isOpen)
            player.hpHud.close()
        player.blockIncomingHits(1)
        player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
    }

    override fun isMultiwayArea(position: Position?): Boolean = true

    fun spawnAcidPool(location: Location) {
        if (araxxor.isDying || araxxor.isDead || araxxor.isFinished) return
        val pool = AcidPool(location)
        poolObjects.add(pool)
        World.spawnObject(pool)
    }

    fun spawnAraxxorAndEggs() {
        AraxytePattern(this)
            .getRandomPattern().forEach {
                it.spawn()
                it.radius = 0
                it.aggressionDistance = 96
                araxytes.add(it)
            }
        araxxor.primaryAraxyte = araxytes[0]
        araxxor.setTransformationPreservingStats(ARAXXOR)
        araxxor.spawn()
        araxxor.lock()
        araxxor seq 11482
        schedule(2) {
            araxxor.faceEntity(player)
            araxxor.setTarget(player)
            araxxor.unlock()
            player!!.bossTimer.startTracking("Araxxor")
        }
    }

    private fun transferPlayer() {
        if (player == null) return
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entranceLocation)
            player.faceDirection(Direction.WEST)
            player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel."))
        }
        player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            player.unlock()
            schedule(4) {
                spawnAraxxorAndEggs()
                player.hpHud.open(ARAXXOR, araxxor.maxHitpoints)
            }
        }
    }

    override fun enter(player: Player) {
        player.mapInstance = this@AraxxorInstance
        player.teleport(entranceLocation)
        player.faceEntity(araxxor)
    }

    fun getAcidSplatterLocation(location: Location?): Location {
        val xOffset = Random.nextInt(-3, 3)
        val yOffset = Random.nextInt(-3, 3)
        val tempLocation = Location(location?.offset(Pair(xOffset, yOffset)))
        return tempLocation
    }
}
