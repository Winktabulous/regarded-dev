package com.near_reality.game.content.dt2.area

import com.near_reality.game.content.damage
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.CorruptedSeed
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack.safePath
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack.safeTiles
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack.shadowRealmLeeches
import com.near_reality.game.content.hit
import com.near_reality.game.content.offset
import com.near_reality.game.content.sanity
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.TENTACLE_12208
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.plugins.dialogue.PlainChat
import java.util.*
import kotlin.random.Random

data class WhispererInstance(
    val difficulty: DT2BossDifficulty,
    val player: Player
) : PrebuiltDynamicArea(allocatedArea = MapBuilder.findEmptyChunk(16, 16), region = 9830),
    PartialMovementPlugin,
    CannonRestrictionPlugin {
    override fun name(): String = "Whisperer Instance"
    val whisperer: WhispererCombat = WhispererCombat(getLocation(2655 -128, 6367 +192))

    override fun isMultiwayArea(position: Position?): Boolean = true

    override fun constructed() {
        spawnBarrier()
        whisperer.spawn()
        transferPlayer()
        whisperer.setFaceEntity(player)
        whisperer.setTarget(player)
    }

    private fun transferPlayer() {
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entranceTile)
            player.faceDirection(Direction.WEST)
            player.dialogueManager.start(PlainChat(player, "You approach the figure in the water."))
        }
        player.dialogueManager.start(PlainChat(player, "You approach the figure in the water.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            player.unlock()
        }
    }

    private val entranceTile: Location get() = getLocation(2656 -128, 6564)

    override fun enter(player: Player) {
        player.mapInstance = this@WhispererInstance
        player.teleport(entranceTile)
        player.faceEntity(whisperer)
    }

    override fun leave(player: Player, logout: Boolean) = player.hpHud.close()

    private fun spawnBarrier() {
        var xOffset = 0
        repeat(5) {
            xOffset += 2
            val spawnLocation = getLocation(Location(2522, 6575) offset Pair(xOffset, 0))
            val tentacle = NPC(TENTACLE_12208, spawnLocation, Direction.NORTH, 0, true)
            tentacle.spawn()
        }
    }

    override fun processMovement(player: Player?, x: Int, y: Int): Boolean {
        if (player != null) {
            if (!whisperer.isDead || !whisperer.isFinished) {
                if (whisperer.state.usingSpecial) {
                    val trackedTile = player.location
                    val specialAttack = whisperer.getSpecialAttack()
                    // If  Whisperer is using the Leech Attack
                    if (specialAttack is ShadowLeachSpecialAttack) {
                        val realWorldLeech = World.getObjectWithId(trackedTile, CORRUPTED_SEED_REAL_WORLD)
                        val darkGreenLeechObject = World.getObjectWithId(trackedTile, CORRUPTED_SEED_DARK_GREEN)
                        val lightGreenLeechObject = World.getObjectWithId(trackedTile, CORRUPTED_SEED_LIGHT_GREEN)
                        val tileSteppedOn = getLocation(trackedTile)

                        // we need to check if we step on a tile of the safe path
                        if (specialAttack.safePath.contains(tileSteppedOn) && lightGreenLeechObject != null) {
                            specialAttack.removeLeech(player, whisperer, 2465, lightGreenLeechObject, tileSteppedOn)
                            specialAttack.safePath.remove(tileSteppedOn)
                        }
                        else if (!specialAttack.safePath.contains(tileSteppedOn) && realWorldLeech != null)
                            specialAttack.removeLeech(player, whisperer, 2462, lightGreenLeechObject, tileSteppedOn)
                        else if (darkGreenLeechObject != null)
                            specialAttack.removeLeech(player, whisperer, 2462, lightGreenLeechObject, tileSteppedOn)
                    }
                }
            }
        }
        return true
    }
}