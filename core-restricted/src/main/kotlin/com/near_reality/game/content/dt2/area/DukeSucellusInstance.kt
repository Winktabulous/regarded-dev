package com.near_reality.game.content.dt2.area

import com.zenyte.game.task.TickTask
import com.zenyte.game.world.World
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.DelayRepeatTask
import com.near_reality.game.content.dt2.area.obj.FermentationVat
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.theduke.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispyUtils.rand
import com.zenyte.game.content.boons.impl.AllGassedUp
import com.zenyte.game.content.skills.slayer.SlayerEquipment
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin
import com.zenyte.plugins.dialogue.PlainChat
import io.mockk.InternalPlatformDsl.toArray
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class DukeSucellusInstance(val difficulty: DT2BossDifficulty, val player: Player) : PrebuiltDynamicArea(REGION_ID), CycleProcessPlugin {

    var hasReceivedPestle: Boolean = false
    var hasReceivedPickaxe: Boolean = false

    lateinit var duke: DukeSucellusEntity

    val leftVat: FermentationVat = FermentationVat(this)

    val rightVat: FermentationVat = FermentationVat(this)

    private val leftVatCoord: Location by lazy {
        getLocation(leftVatNormalCoord)
    }

    private val rightVatCoord: Location by lazy {
        getLocation(rightVatNormalCoord)
    }

    private val rightTopExtremityCoord: Location by lazy {
        getLocation(rightTopExtremityNormalCoord)
    }

    private val rightMiddleExtremityCoord: Location by lazy {
        getLocation(rightMiddleExtremityNormalCoord)
    }

    private val rightBottomExtremityCoord: Location by lazy {
        getLocation(rightBottomExtremityNormalCoord)
    }

    private val leftTopExtremityCoord: Location by lazy {
        getLocation(leftTopExtremityNormalCoord)
    }

    private val leftMiddleExtremityCoord: Location by lazy {
        getLocation(leftMiddleExtremityNormalCoord)
    }

    private val leftBottomExtremityCoord: Location by lazy {
        getLocation(leftBottomExtremityNormalCoord)
    }

    val leftBound: Location by lazy {
        getLocation(leftBoundNormal)
    }

    val rightBound: Location by lazy {
        getLocation(rightBoundNormal)
    }





    private val extremityGroups : Object2ObjectArrayMap<ExtremitySide, ExtremityGroup> = Object2ObjectArrayMap()

    private var gasVentRotation = 0
    private var gasVentCooldown = rand(35, 50)

    lateinit var ventPositions: List<Location>

    private val gasVentPatterns = mutableMapOf(
        0 to List(3) { Location(0, 0) },
        1 to List(3) { Location(0, 0) },
        2 to List(3) { Location(0, 0) }
    )

    private val entranceTile: Location get() = getLocation(normalEntranceTile)

    override fun enter(player: Player) = with(player) {
        player.mapInstance = this@DukeSucellusInstance
        teleport(entranceTile)
    }

    override fun leave(player: Player, logout: Boolean) = with(player) {
        super.leave(player, logout)
        removeFarmedMaterials()
        if(logout) setLocation(getLocation(normalExitTile))
        else player.hpHud.close()
    }

    override fun name(): String = "Duke Sucellus"

    override fun constructed() {
        removeFarmedMaterials()
        addNpcs()
        buildExtremities()
        transferPlayer()
    }

    private fun removeFarmedMaterials() {
        player.inventory.container.findAllById(ItemId.ARDERMUSCA_POISON).values.forEach { player.inventory.container.remove(it) }
        player.inventory.container.findAllById(ItemId.SALAX_SALT).values.forEach { player.inventory.container.remove(it) }
        player.inventory.container.findAllById(ItemId.ARDER_MUSHROOM).values.forEach { player.inventory.container.remove(it) }
        player.inventory.container.findAllById(ItemId.ARDER_POWDER).values.forEach { player.inventory.container.remove(it) }
        player.inventory.container.findAllById(ItemId.MUSCA_POWDER).values.forEach { player.inventory.container.remove(it) }
        player.inventory.container.findAllById(ItemId.MUSCA_POWDER).values.forEach { player.inventory.container.remove(it) }
    }

    private fun buildExtremities() {
        val left = ExtremityGroup(ExtremitySide.LEFT, this,
            members = listOf(
                Extremity(leftTopExtremityCoord, 47546, 47547, 3),
                Extremity(leftMiddleExtremityCoord, 47544, 47548, 3),
                Extremity(leftBottomExtremityCoord, 47545, 47549, 3)
            )
        )

        val right = ExtremityGroup(ExtremitySide.RIGHT, this,
            members = listOf(
                Extremity(rightTopExtremityCoord, 47545, 47549, 1),
                Extremity(rightMiddleExtremityCoord, 47546, 47547, 1),
                Extremity(rightBottomExtremityCoord, 47544, 47548, 1)
            )
        )

        extremityGroups[left.side] = left
        extremityGroups[right.side] = right
    }

    private fun transferPlayer() {
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entranceTile)
            player.faceDirection(Direction.WEST)
            player.dialogueManager.start(PlainChat(player, "You enter the asylum."))
        }
        player.dialogueManager.start(PlainChat(player, "You enter the asylum.", false))
        screen.fade()
        WorldTasksManager.schedule({ screen.unfade() }, 2)
        player.unlock()
    }

    fun fillVat(player: Player, loc: WorldObject) = when {
        loc.position.matches(leftVatCoord) -> leftVat.fill(player, loc)
        loc.position.matches(rightVatCoord) -> rightVat.fill(player, loc)
        else -> {player.sendDeveloperMessage("Unable to find which vat to fill")}
    }

    fun checkVat(player: Player, loc: WorldObject) = when {
        loc.position.matches(leftVatCoord) -> leftVat.check(player)
        loc.position.matches(rightVatCoord) -> rightVat.check(player)
        else -> {player.sendDeveloperMessage("Unable to find which vat to check")}
    }

    private var awakened : Boolean = false

    fun dukeAwakened() {
        awakened = true
        awakenExtremities()
    }

    private fun awakenExtremities() {
        extremityGroups.values.forEach {
            it.awaken()
        }
    }

    fun dukeGoodnight() {
        awakened = false
        sleepExtremities()
    }

    private fun sleepExtremities() {
        extremityGroups.values.forEach {
            it.sleep()
        }
    }


    /**
     * Populates the constructed map instance with the initial necessary npcs.
     */
    private fun addNpcs() {
        duke = DukeSucellusEntity(arena = this)
        addNpc(duke.spawn())

        val ventTiles = mutableListOf<Location>()
        var row = 0
        var col = 0
        repeat(9) {
            if (col > 0 && col % 3 == 0) {
                col = 0
                row++
            }

            val coords = swGasVentCoord.copy().transform(col * 3, row * 4)
            val vent = GasVent() spawnAt (coords relativeTo this)
            addNpc(vent)

            val offset = vent.size / 2
            ventTiles += vent.location.transform(offset, offset).copy()
            col++
        }

        repeat(3) {
            when (it) {
                0 -> {
                    gasVentPatterns[it] = listOf(ventTiles[2].copy(), ventTiles[4].copy(), ventTiles[6].copy())
                }

                1 -> {
                    gasVentPatterns[it] = listOf(ventTiles[0].copy(), ventTiles[5].copy(), ventTiles[7].copy())
                }

                else -> {
                    gasVentPatterns[it] = listOf(ventTiles[1].copy(), ventTiles[3].copy(), ventTiles[8].copy())
                }
            }
        }

        ventPositions = ventTiles
    }

    companion object {

        private const val REGION_ID = 12132

        private val swGasVentCoord = Location(3036, 6442)

        val normalEntranceTile: Location = Location(3039, 6435)

        val normalExitTile: Location = Location(3039, 6432)

        val leftVatNormalCoord: Location = Location(3034, 6438)

        val rightVatNormalCoord: Location = Location(3043, 6438)

        val rightTopExtremityNormalCoord: Location = Location(3049,6449)
        val rightMiddleExtremityNormalCoord: Location = Location(3049,6445)
        val rightBottomExtremityNormalCoord: Location = Location(3049,6441)
        val leftTopExtremityNormalCoord: Location = Location(3028,6449)
        val leftMiddleExtremityNormalCoord: Location = Location(3028,6445)
        val leftBottomExtremityNormalCoord: Location = Location(3028,6441)

        private val leftBoundNormal: Location =
            Location(3035, 6451)
        private val rightBoundNormal: Location =
            Location(3043, 6451)



        fun createInstance(diff: DT2BossDifficulty, p: Player) : DukeSucellusInstance {
            return DukeSucellusInstance(diff, p)
        }
    }

    override fun process() {
        if(awakened) {
            for (extremityGroup in extremityGroups.values) {
                extremityGroup.process()
            }
            if(gasVentCooldown == 0) {
                runGasVents()
                gasVentCooldown = rand(45, 75)
            } else {
                gasVentCooldown--
            }
        }
    }

    var passingGas = false

    private fun runGasVents() {
        passingGas = true
        WorldTasksManager.schedule(object : TickTask() {
            var cycle: Int = 0
            var startTick: Int = 1
            override fun run() {
                if(!awakened) {
                    passingGas = false
                    stop()
                }
                when(ticks) {
                    startTick -> { gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2431), vent) } }
                    startTick + 1 -> { gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2432), vent) } }
                    startTick + 2 -> { gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2433), vent) } }
                    startTick + 3 -> {
                        gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2431), vent) }
                        if(!(SlayerEquipment.NOSE_PEG.isWielding(player) && player.hasBoon(AllGassedUp::class.java))) {
                            gasVentPatterns[gasVentRotation]?.forEach { vent ->
                                player.let { if (it.position.withinDistance(vent, 1)) it.applyHit(Hit(rand(5, 11), HitType.POISON)) }
                            }
                        }

                    }
                    startTick + 4 -> {
                        gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2432), vent) }
                        if(!(SlayerEquipment.NOSE_PEG.isWielding(player) && player.hasBoon(AllGassedUp::class.java))) {
                            gasVentPatterns[gasVentRotation]?.forEach { vent ->
                                player.let { if (it.position.withinDistance(vent, 1)) it.applyHit(Hit(rand(5, 11), HitType.POISON)) }
                            }
                        }
                    }
                    startTick + 5 -> {
                        gasVentPatterns[gasVentRotation]?.forEach { vent -> World.sendGraphics(Graphics(2433), vent) }
                        if(!(SlayerEquipment.NOSE_PEG.isWielding(player) && player.hasBoon(AllGassedUp::class.java))) {
                            gasVentPatterns[gasVentRotation]?.forEach { vent ->
                                player.let { if (it.position.withinDistance(vent, 1)) it.applyHit(Hit(rand(5, 11), HitType.POISON)) }
                            }
                        }
                    }
                    startTick + 6 -> {
                        startTick = ticks + 3
                        rotateGasVent()
                        cycle++
                    }
                    99999 -> {
                        passingGas = false
                        stop()
                    }
                }
                ticks++

                if(cycle == 3)
                    ticks = 99999
            }
        }, 0, 0)

    }

    private fun rotateGasVent() {
        gasVentRotation = (gasVentRotation + 1) % 3
    }


}

private infix fun NPC.spawnAt(coords: Location): NPC {
    return NPC(this.id, coords.transform(-1, -1), Direction.SOUTH, 0, true).spawn()
}

private infix fun Location.relativeTo(region: DynamicArea): Location {
    return region.getLocation(this)
}
