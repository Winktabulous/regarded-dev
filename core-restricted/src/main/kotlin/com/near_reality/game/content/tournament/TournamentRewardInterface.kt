package com.near_reality.game.content.tournament

import com.near_reality.game.content.shop.ShopCurrencyHandler.getAmount
import com.near_reality.game.content.shop.ShopCurrencyHandler.remove
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import mgi.types.config.enums.Enums
import kotlin.math.min

@Suppress("unused")
class TournamentRewardInterface : Interface() {
    override fun attach() {
        put(6, "Buy reward")
    }

    override fun build() {
        bind("Buy reward") { player: Player, slotId: Int, itemId: Int, option: Int ->
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId)
                return@bind
            }
            val realItemId = Enums.TOURNAMENT_REWARDS.getValueOrDefault(slotId)

            if (player.isIronman) {
                val itemIsIronman = Enums.TOURNAMENT_REWARDS_IRONMAN.getValueOrDefault(realItemId) == 1
                if (!itemIsIronman) {
                    player.sendMessage("You can't purchase this item as an Ironman.")
                    return@bind
                }
            }

            val amount = when (option) {
                2 -> 1
                3 -> 5
                4 -> 10
                else -> 50
            }
            val cost = Enums.TOURNAMENT_REWARDS_COST.getValueOrDefault(realItemId)
            val points = getAmount(ShopCurrency.TOURNAMENT_POINTS, player)
            val affordableAmount = min((points / cost).toDouble(), amount.toDouble()).toInt()
            if (affordableAmount <= 0) {
                player.sendMessage("You don't have enough Tournament points to purchase this.")
                return@bind
            }
            if (affordableAmount < amount) player.sendMessage("You don't have enough Tournament points to purchase this many.")
            val itemAmount = Enums.TOURNAMENT_REWARDS_NUM.getValueOrDefault(realItemId)
            val result = player.inventory.addItem(Item(realItemId, affordableAmount * itemAmount))
            val amountBought = result.succeededAmount / itemAmount
            remove(ShopCurrency.TOURNAMENT_POINTS, player, amountBought * cost)
            player.varManager.sendVar(261, getAmount(ShopCurrency.TOURNAMENT_POINTS, player))
            if (result.result == RequestResult.NOT_ENOUGH_SPACE) player.sendMessage("Not enough space in your inventory.")
        }
    }

    override fun open(player: Player) {
        player.varManager.sendVar(261, getAmount(ShopCurrency.TOURNAMENT_POINTS, player))
        player.varManager.sendVar(262, if (player.isIronman) 1 else 0)

        super.open(player)

        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            getComponent("Buy reward"),
            0,
            Enums.TOURNAMENT_REWARDS.size,
            AccessMask.CLICK_OP2,
            AccessMask.CLICK_OP3,
            AccessMask.CLICK_OP4,
            AccessMask.CLICK_OP5,
            AccessMask.CLICK_OP10
        )
    }

    override fun getInterface(): GameInterface =
        GameInterface.TOURNAMENT_SHOP
}
