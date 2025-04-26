package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player

/**
 * @author Tommeh
 * @author Jire
 */
class VerzikCrystalShard : ItemPlugin() {

    override fun handle() {
        bind("Activate") { player: Player, _, _ ->
            if (player.area !is TheatreRoom) {
                player.sendMessage("The crystal shard seems inert outside the Theatre of Blood.")
                return@bind
            }
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            player.inventory.deleteItem(verzikCrystalShard)
            TeleportCollection.VERZIK_CRYSTAL_SHARD.teleport(player)
            WorldTasksManager.schedule(object : TickTask() {
                override fun run() {
                    when (ticks++) {
                        1 -> PartyOverlayInterface.fadeRedPortal(player, "The crystal teleports you out.")
                        3 -> {
                            party.removeMember(player)
                            PartyOverlayInterface.fade(player, 200, 0, "The crystal teleports you out.")
                            stop()
                        }
                    }
                }
            }, 0, 0)
        }
    }

    override fun getItems() = VerzikCrystalShard.items

    internal companion object {

        val verzikCrystalShard = Item(ItemId.VERZIKS_CRYSTAL_SHARD)

        private val items = intArrayOf(ItemId.VERZIKS_CRYSTAL_SHARD)

    }

}