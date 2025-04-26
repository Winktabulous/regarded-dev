package com.near_reality.game.content.pvm_arena.area

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin

/**
 * This represents the lobby of the PvM Arena activity,
 * players can join one of the teams from here,
 * and are returned here on death.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaLobbyArea :
    PolygonRegionArea(),
    CannonRestrictionPlugin
{
    override fun enter(player: Player?) {
        player?.sendDeveloperMessage("You can use ::managepvm to control this activity.")
    }

    override fun leave(player: Player?, logout: Boolean) {
        player?.protectionDelay = 0
    }

    override fun name(): String =
        "PvM Arena Lobby"

    override fun polygons(): Array<RSPolygon> =
        arrayOf(RSPolygon(1752, 4694, 1771, 4715))

    companion object {

        private val TELEPORT_LOCATION = Location(1761, 4709, 0)

        fun teleportInto(player: Player) {
            if (!player.isLocked) {
                val teleport: Teleport = object : Teleport {
                    override fun getType(): TeleportType = TeleportType.ECTOPHIAL
                    override fun getDestination(): Location = TELEPORT_LOCATION
                    override fun getLevel(): Int = 0
                    override fun getExperience(): Double = 0.0
                    override fun getRandomizationDistance(): Int = 2
                    override fun getRunes(): Array<Item> = emptyArray()
                    override fun getWildernessLevel(): Int = 0
                    override fun isCombatRestricted(): Boolean = true
                }
                teleport.teleport(player)
            } else
                player.sendMessage("You cannot do this at the moment.")
        }

        fun moveInto(player: Player) {
            val location = when (PvmArenaFightArea.findAreaForLocation(player.location)) {
                PvmArenaBlueFightArea -> Location(1763, 4706, 0)
                PvmArenaRedFightArea -> Location(1759, 4706, 0)
                null -> TELEPORT_LOCATION.copy()
            }
            val bandagesInInventory = player.inventory.getAmountOf(ItemId.BANDAGES_25730)
            if (bandagesInInventory > 0){
                player.inventory.deleteItem(Item(ItemId.BANDAGES_25730, bandagesInInventory))
                player.sendMessage("You have lost your bandages.")
            }
            player.blockIncomingHits(5)
            player.setLocation(location)
        }

        fun messagePlayers(message: String) {
            GlobalAreaManager.getArea(PvmArenaLobbyArea::class.java).players.forEach {
                it.sendMessage(message)
            }
        }
    }
}
