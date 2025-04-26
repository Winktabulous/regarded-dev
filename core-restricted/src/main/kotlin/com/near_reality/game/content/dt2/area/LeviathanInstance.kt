package com.near_reality.game.content.dt2.area

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.leviathan.LeviathanCombat
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder

class LeviathanInstance(
    allocatedArea: AllocatedArea,
    val player: Player,
    val difficulty: DT2BossDifficulty = DT2BossDifficulty.NORMAL,
    private val debugMechanics : Boolean = true
) : DynamicArea(allocatedArea, 137, 424),
    FullMovementPlugin,
    CannonRestrictionPlugin {

    private var leviathan: LeviathanCombat? = null

    override fun name(): String = "Leviathan Instance"

    companion object {
        fun createInstance(p: Player) : LeviathanInstance {
            val area: AllocatedArea = MapBuilder.findEmptyChunk(8, 6)
            return LeviathanInstance(area, p)
        }

        fun outside() = outsideLocation
        fun inside() = insideLocation

        private val insideLocation = Location(2071, 6368, 0)
        private val outsideLocation = Location(2069, 6368, 0)
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {
        if(player.hpHud.isOpen)
            player.hpHud.close()
        player.blockIncomingHits(1)
        player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
        player.packetDispatcher.sendClientScript(7021, 255)
        if (logout)
            player.forceLocation(outside())
    }

    override fun destroyRegion() {
        super.destroyRegion()
        if(leviathan != null) {
            leviathan!!.finish()
            leviathan = null
        }
    }


    override fun constructed() {
        leviathan = LeviathanCombat(this.getLocation(2078, 6369, 0), this)
        leviathan!!.spawn()

        transferPlayer()
    }

    private fun transferPlayer() {
        player.setLocation(getLocation(inside()))
        player.faceDirection(Direction.EAST)
        player.sendMessage("You scale the small rocks.")
    }

    fun debugMechanic(msg: String) {
        if (debugMechanics) player.sendDeveloperMessage(msg)
    }

    override fun processMovement(player: Player?, x: Int, y: Int): Boolean {
        return true
    }
}

