package com.zenyte.game.content.minigame.duelarena.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.world.entity.player.Player
import java.util.*

class StakingInventoryInterface : Interface() {

    override fun attach() {
        put(0, "My Stakeable Inventory")
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) {
    }

    override fun open(player: Player) {
    }

    override fun build() {
        bind("My Stakeable Inventory") { player: Player, slotId: Int, itemId: Int, option: Int ->
            val itemAtSlot = player.inventory.getItem(slotId)
            if (!itemAtSlot.isTradable)
            {
                player.sendMessage("You can't stake this item.")
                return@bind
            }
            val duel = player.duel
            val stakeOption = when(option){
                1 -> ItemStakeOption.Amount(1)
                2 -> ItemStakeOption.Amount(5)
                3 -> ItemStakeOption.Amount(10)
                4 -> ItemStakeOption.Amount(player.inventory.getAmountOf(itemId))
                5 -> ItemStakeOption.X
                else -> return@bind
            }
            when(stakeOption) {
                is ItemStakeOption.Amount -> {
                    duel.addItem(itemId, stakeOption.amount)
                }
                ItemStakeOption.X -> {
                    player.sendInputInt("Enter amount:") { amountEntered: Int ->

                        val amount = amountEntered.coerceAtMost(player.inventory.getAmountOf(itemId))
                        if (amount <= 0)
                            return@sendInputInt

                        duel.addItem(itemId, amount)
                    }
                }
            }

        }
    }

    override fun getInterface() = GameInterface.DUEL_STAKING_INVENTORY
}