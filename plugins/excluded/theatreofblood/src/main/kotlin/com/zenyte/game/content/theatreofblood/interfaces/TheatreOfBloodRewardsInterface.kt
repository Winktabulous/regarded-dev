package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.reward.RewardRoom
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.dialogue.options

class TheatreOfBloodRewardsInterface : Interface() {

	override fun attach() {
		put(6, "Bank all")
		put(8, "Take all")
		put(10, "Discard all")
		put(12, "Take item")
	}

	override fun open(player: Player) {
		super.open(player)

		player.packetDispatcher.sendComponentSettings(getInterface(), getComponent("Take item"), 0, Container.getSize(ContainerType.THEATRE_OF_BLOOD), AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP10)
		player.packetDispatcher.sendClientScript(150, id shl 16 or getComponent("Take item"), ContainerType.THEATRE_OF_BLOOD.id, 2, 3, 0, -1, "Take", "", "", "", "", "", "", "", "")
	}

	override fun build() {
		bind("Bank all") { player ->
			player.sendMessage("This feature is currently disabled.")
//			val party = VerSinhazaArea.getParty(player) ?: return@bind
//			party.raid ?: return@bind
//			val room = player.area as? RewardRoom ?: return@bind
//			val container: Container = room.playersLoot[player] ?: return@bind
//			if (container.isEmpty) {
//				player.sendMessage("Your reward chest is empty.")
//				return@bind
//			}
//			for (slot in 0 until (container.containerSize + 1)) {
//				val item = container[slot] ?: continue
//				container.withdraw(player, player.bank.container, slot, item.amount)
//			}
//			container.refresh(player)
//			player.bank.container.refresh(player)
		}
		bind("Take all") { player ->
			val party = VerSinhazaArea.getParty(player) ?: return@bind
			party.raid ?: return@bind
			val room = player.area as? RewardRoom ?: return@bind
			val container: Container = room.playersLoot[player] ?: return@bind
			if (container.isEmpty) {
				player.sendMessage("Your reward chest is empty.")
				return@bind
			}
			for (slot in 0 until (container.containerSize + 1)) {
				val item = container[slot] ?: continue
				container[slot] = item.toNote()//Little hack to note items on withdraw but display without a note
				player.inventory.container.deposit(player, container, slot, item.amount)
				if (container[slot] != null) {
					container[slot] = Item(item.definitions.unnotedOrDefault, item.amount)
				}
			}
			container.refresh(player)
			player.inventory.container.refresh(player)
		}
		bind("Discard all") { player ->
			player.options("Are you sure you want to destroy the items?") {
				"<col=ff0000>DESTROY!" {
					val party = VerSinhazaArea.getParty(player)
					if (party?.raid != null) {
						val room = player.area as? RewardRoom
						if (room != null) {
							val container: Container? = room.playersLoot[player]
							if (container != null) {
								container.clear()
								container.refresh(player)
							}
						}
					}
				}
				"Cancel" {

				}
			}
		}
		bind("Take item") { player, slotId, itemId, option ->
			val party = VerSinhazaArea.getParty(player) ?: return@bind
			party.raid ?: return@bind
			val room = player.area as? RewardRoom ?: return@bind
			val container: Container = room.playersLoot[player] ?: return@bind
			if (option == 10) {
				ItemUtil.sendItemExamine(player, itemId)
				return@bind
			}
			val item = container[slotId] ?: return@bind
			container[slotId] = item.toNote()//Little hack to note items on withdraw but display without a note
			container.withdraw(player, player.inventory.container, slotId, item!!.amount)
			if (container[slotId] != null) {
				container[slotId] = Item(item.definitions.unnotedOrDefault, item.amount)
			}
			container.refresh(player)
			player.inventory.container.refresh(player)
		}
	}

	override fun getInterface() = GameInterface.TOB_REWARDS

}
