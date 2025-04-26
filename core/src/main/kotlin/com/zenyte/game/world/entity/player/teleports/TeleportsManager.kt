package com.zenyte.game.world.entity.player.teleports

import com.near_reality.cache.interfaces.teleports.Category
import com.near_reality.cache.interfaces.teleports.Destination
import com.zenyte.game.GameInterface
import com.zenyte.game.model.item.containers.HerbSack
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Jire
 */
class TeleportsManager(
    @Transient
    private val player: Player
) {

    @Transient
    var selectedCategory: Category? = null

    @Transient
    var searchSelected: Boolean = false

    @Transient
    var previousDestination: Destination? = null

    var favoriteDestinations: MutableList<Int> = ArrayList()

    fun initialize(teleportsManager: TeleportsManager) {
        favoriteDestinations = teleportsManager.favoriteDestinations
    }

    fun setPreviousTeleport(destination: Destination) {
        previousDestination = destination
        player.packetDispatcher.sendClientScript(10592, destination.name)
    }

    fun attemptTeleport(destination: Destination) {
        if (destination == null) {
            player.sendMessage("You don't have anywhere to teleport to.")
            return
        }

        if (player.isLocked || player.interfaceHandler.containsInterface(InterfacePosition.CENTRAL)) {
            player.sendMessage("You can't do that right now.")
            return
        }

        DestinationTeleport(destination).teleport(player)
    }

    fun attemptOpen() {
        if (player.isLocked) {
            player.sendMessage("You can't do that right now.")
            return
        }
        GameInterface.TELEPORTS.open(player)
    }

    fun updateFavorites() {
        var favoriteData = IntArray(7)

        if (favoriteDestinations != null) {
            for (destinationId in favoriteDestinations) {
                val index = destinationId shr 5
                val pos = destinationId and 31
                if (index >= favoriteData.size) {
                    favoriteDestinations.clear()
                    break
                } else {
                    favoriteData[index] = favoriteData[index] or (1 shl pos)
                }
                //println("tele=$destinationId, var=$index, bit=$pos, value=${favoriteData[index]}, mask=${1 shl pos}")
            }
        }

        //println(favoriteData.contentToString())

        player.varManager.sendVar(263, favoriteData[0])
        player.varManager.sendVar(264, favoriteData[1])
        player.varManager.sendVar(265, favoriteData[2])
        player.varManager.sendVar(266, favoriteData[3])
        player.varManager.sendVar(262, favoriteData[4])
        player.varManager.sendVar(3808, favoriteData[5])
        player.varManager.sendVar(3809, favoriteData[6])
    }

}