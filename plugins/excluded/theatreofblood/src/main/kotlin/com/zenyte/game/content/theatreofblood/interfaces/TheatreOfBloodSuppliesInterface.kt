package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.Colour
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import mgi.types.config.enums.Enums
import mgi.types.config.items.ItemDefinitions
import kotlin.math.max

/**
 * @author Jire
 */
class TheatreOfBloodSuppliesInterface : Interface() {

    override fun attach() = put(8, "Buy")

    override fun build() {
        bind("Buy") { player, slotId, _, option ->
            if (slotId == 0) {
                player.sendMessage("This item is out of stock.")
                return@bind
            }

            val item = Enums.TOB_SUPPLIES_SLOT_TO_ITEM.getValueOrDefault(slotId)
            if (item == -1) return@bind
            val cost = Enums.TOB_SUPPLIES_ITEM_TO_COST.getValueOrDefault(item)
            if (cost == -1) return@bind

            val points = player.tobPoints

            when (option) {
                1 -> player.sendMessage(
                    "${ItemDefinitions.nameOf(item)} costs ${
                        Utils.conditionallyColorized(
                            points >= cost,
                            Utils.pluralized("point", cost),
                            Colour.RS_GREEN,
                            Colour.RS_RED
                        )
                    }."
                )
                2 -> player.buy(item, 1, cost)
                3 -> player.buy(item, max(1, points / cost), cost)
                4 -> player.sendInputInt("Enter amount:") { amount -> player.buy(item, amount, cost) }
                10 -> ItemUtil.sendItemExamine(player, item)
            }
        }
    }

    override fun open(player: Player) {
        super.open(player)

        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            getComponent("Buy"),
            0,
            8,
            AccessMask.CLICK_OP1,
            AccessMask.CLICK_OP2,
            AccessMask.CLICK_OP3,
            AccessMask.CLICK_OP4,
            AccessMask.CLICK_OP10
        )
    }

    override fun getInterface() = GameInterface.TOB_CHEST_SUPPLIES

    internal companion object {

        private const val POINTS_VARBIT = 6460

        var Player.tobPoints
            get() = varManager.getBitValue(POINTS_VARBIT)
            set(value) = varManager.sendBit(POINTS_VARBIT, value)

        private fun Player.buy(item: Int, amount: Int, cost: Int) {
            if (amount < 1) return

            val points = tobPoints

            val pointsReq = amount * cost
            if (pointsReq > points) {
                sendMessage(
                    "You don't have enough points to buy ${
                        Utils.pluralized(
                            ItemDefinitions.nameOf(item),
                            amount
                        )
                    }."
                )
                return
            }

            for (i in 0 until amount) {
                if (inventory.addItem(item, 1).isFailure) {
                    sendMessage("You don't have enough space in your inventory to purchase this item.")
                    break
                } else tobPoints -= cost
            }
        }

    }

}