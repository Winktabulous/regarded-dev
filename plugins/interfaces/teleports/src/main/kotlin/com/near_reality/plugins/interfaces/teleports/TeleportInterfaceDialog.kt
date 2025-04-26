package com.near_reality.plugins.interfaces.teleports

import com.near_reality.cache.interfaces.teleports.Destination
import com.near_reality.cache.interfaces.teleports.TeleportsList
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Analytics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.teleports.DestinationTeleport
import com.zenyte.plugins.dialogue.CountDialogue

class TeleportInterfaceDialog : CountDialogue {

	override fun run(index: Int) = Unit

	override fun execute(player: Player, index: Int) {
		if (index < 0) {
			return
		}
		if (player.temporaryAttributes["interfaceInput"] !== this) {
			return
		}

		val favorite = (index and 0x10000) != 0
		val search = (index and 0x20000) != 0
		val index = index and 0xffff

		val destination: Destination = if (search) {
			TeleportsList.allTeleports[index]
		} else {
			val categoryDestinations = player.teleportsManager.selectedCategory!!.destinations
			val extraSize = if (player.teleportsManager.favoriteDestinations != null) player.teleportsManager.favoriteDestinations.size else 0
			val destinations: MutableList<Destination> = ArrayList(categoryDestinations.size+extraSize)
			if (player.teleportsManager.favoriteDestinations != null) {
				val favorites = player.teleportsManager.favoriteDestinations.toMutableList()
				favorites.sort()
				for (favorite in favorites) {
					destinations.add(TeleportsList.allTeleports[favorite])
				}
			}
			destinations.addAll(categoryDestinations)
			destinations[index]
		}

		//println("index=$index, $favorite $search, ${TeleportsList.allTeleports.indexOf(destination)}")
		if (favorite) {
			val listIndex = TeleportsList.allTeleports.indexOf(destination)
			if (listIndex == -1) {
				player.sendDeveloperMessage("Index is -1 for teleport?")
				return
			}

			val index = listIndex shr 5
			if (index > 6) {
				player.sendDeveloperMessage("Out of bounds teleporting trying to favorite: $index")
				return
			}

			if (player.teleportsManager.favoriteDestinations == null) {
				player.teleportsManager.favoriteDestinations = ArrayList()
			}

			if (player.teleportsManager.favoriteDestinations.contains(listIndex)) {
				player.teleportsManager.favoriteDestinations.remove(listIndex)
			} else {
				player.teleportsManager.favoriteDestinations.add(listIndex)
			}

			player.interfaceHandler.closeInterface(InterfacePosition.CENTRAL)
			player.teleportsManager.attemptOpen()
			return
		}

		Analytics.flagInteraction(player, Analytics.InteractionType.TELEPORT_INTERFACE)
		DestinationTeleport(destination).teleport(player)
		player.teleportsManager.setPreviousTeleport(destination)
		player.interfaceHandler.closeInterface(InterfacePosition.CENTRAL)
		player.interfaceHandler.forceCloseInput()
	}

}