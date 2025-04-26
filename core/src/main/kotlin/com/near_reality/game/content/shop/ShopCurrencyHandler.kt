package com.near_reality.game.content.shop

import com.near_reality.api.service.vote.totalVoteCredits
import com.near_reality.game.world.entity.player.bountyHunterPoints
import com.near_reality.game.world.entity.player.exchangePoints
import com.near_reality.game.world.entity.player.pvmArenaPoints
import com.zenyte.game.content.skills.afk.AfkSkillingConstants
import com.zenyte.game.item.Item
import com.zenyte.game.model.shop.CurrencyPalette
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.world.entity.player.Player
import kotlin.math.max
import kotlin.math.min

object ShopCurrencyHandler {

    @JvmStatic
    fun getAmount(type: CurrencyPalette, player: Player): Int {
        return when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits
            ShopCurrency.AFK_POINTS -> player.getNumericAttribute(AfkSkillingConstants.AFK_POINTS).toInt()
            ShopCurrency.BH_POINTS -> player.bountyHunterPoints
            ShopCurrency.LOYALTY_POINTS -> player.loyaltyManager.loyaltyPoints
            ShopCurrency.EXCHANGE_POINTS -> player.exchangePoints
            ShopCurrency.TOURNAMENT_POINTS -> player.getNumericAttribute("tournament points").toInt()
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints.toInt()
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    return player.inventory.getAmountOf(itemId)
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }

    @JvmStatic
    fun remove(type: ShopCurrency, player: Player, amount: Int) {
        when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits -= amount
            ShopCurrency.BH_POINTS -> player.bountyHunterPoints -= amount
            ShopCurrency.AFK_POINTS -> player.incrementNumericAttribute(AfkSkillingConstants.AFK_POINTS, -amount)
            ShopCurrency.LOYALTY_POINTS -> {
                val currentAmount = player.loyaltyManager.loyaltyPoints
                player.loyaltyManager.setLoyaltyPoints(max(0, (currentAmount - amount)))
            }
            ShopCurrency.TOURNAMENT_POINTS -> player.incrementNumericAttribute("tournament points", -amount)
            ShopCurrency.EXCHANGE_POINTS -> player.exchangePoints -= amount
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints -= amount
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    player.inventory.deleteItem(Item(itemId, amount))
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }

    @JvmStatic
    fun add(type: ShopCurrency, player: Player, amount: Int) {
        when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits += amount
            ShopCurrency.BH_POINTS -> player.bountyHunterPoints += amount
            ShopCurrency.AFK_POINTS -> player.incrementNumericAttribute(AfkSkillingConstants.AFK_POINTS, amount)
            ShopCurrency.LOYALTY_POINTS -> {
                val currentAmount = player.loyaltyManager.loyaltyPoints
                player.loyaltyManager.setLoyaltyPoints(min((currentAmount + amount), Int.MAX_VALUE))
            }
            ShopCurrency.TOURNAMENT_POINTS -> player.incrementNumericAttribute("tournament points", amount)
            ShopCurrency.EXCHANGE_POINTS -> player.exchangePoints += amount
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints += amount
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    player.inventory.addItem(Item(itemId, amount))
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }
}
