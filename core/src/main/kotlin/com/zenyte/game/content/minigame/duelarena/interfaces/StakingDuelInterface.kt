package com.zenyte.game.content.minigame.duelarena.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.minigame.duelarena.DuelSetting
import com.zenyte.game.content.minigame.duelarena.DuelStage
import com.zenyte.game.content.minigame.duelarena.updateInventories
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.packet.sendItemOptionScript
import com.zenyte.game.util.AccessMask.*
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import java.util.*

class StakingDuelInterface : Interface() {

    override fun attach() {
        put(18, "My Staked Items")
        put(21, "Target name")
        put(70, "Accept")
        put(71, "Decline")
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) {
        if (!replacement.isPresent || replacement.get() != GameInterface.DUEL_CONFIRMATION) {
            player.duel?.close(true)
        }
    }

    override fun open(player: Player) {
        val duel = player.duel?:return
        player.interfaceHandler.apply {
            sendInterface(this@StakingDuelInterface)
            sendInterface(GameInterface.DUEL_STAKING_INVENTORY)
        }
        player.packetDispatcher.apply {
            sendItemOptionScript(
                149, 1998, 0, ContainerType.INVENTORY,
                "Stake 1", "Stake 5", "Stake 10", "Stake All", "Stake X")
            sendItemOptionScript(
                149,  GameInterface.DUEL_STAKING.id, 18, ContainerType.DUEL_STAKE,
                "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X",
                scrollBarComponentId = 19)
            sendClientScript(158,  GameInterface.DUEL_STAKING.id component 26, 134, 4, 7, 0,  GameInterface.DUEL_STAKING.id component  27, "", "", "", "", "", 1)
            sendComponentSettings(1998, 0, 0, 27, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP10)
            sendComponentSettings(GameInterface.DUEL_STAKING.id, 18, 0, 27, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP10)
            sendComponentSettings(GameInterface.DUEL_STAKING.id, 26, 0, 27, CLICK_OP10)
            sendComponentText(getInterface(), getComponent("Target name"), "${duel.opponent.name}\'s stake:")

            sendUpdateItemContainer(134, -1, 64168, duel.getContainer(player))
            sendUpdateItemContainer(134, -2, 60937, duel.getContainer(player.duel.opponent))
            sendUpdateItemContainer(93, -1, 64209, player.inventory.container)

            sendClientScript(158,  GameInterface.DUEL_STAKING.id component 26, 134, 4, 7, 0,  GameInterface.DUEL_STAKING.id component 27, "", "", "", "", "", 1)
            sendClientScript(10684,  GameInterface.DUEL_STAKING.id component 26)

            if (duel.hasRule(DuelSetting.SHOW_INVENTORIES)) {
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 10, false)
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 29, false)
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 30, true)
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 6, true)
                sendComponentSettings(GameInterface.DUEL_STAKING.id, 29, 0, 27, 1024)
                sendComponentSettings(GameInterface.DUEL_STAKING.id, 30, 0, 13, 1024)
                duel.updateInventories()
            } else {
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 10, true)
                sendComponentVisibility(GameInterface.DUEL_STAKING.id, 6, false)
            }
        }
    }


    override fun build() {
        bind("Accept") { player -> player.duel?.confirm(DuelStage.STAKE) }
        bind("Decline") { player -> player.duel?.close(true) }
        bind("My Staked Items") { player: Player, _: Int, itemId: Int, option: Int ->
            val duel = player.duel
            val myContainer = duel.getContainer(player)
            val stakeOption = when(option){
                1 -> ItemStakeOption.Amount(1)
                2 -> ItemStakeOption.Amount(5)
                3 -> ItemStakeOption.Amount(10)
                4 -> ItemStakeOption.Amount(myContainer.getAmountOf(itemId))
                5 -> ItemStakeOption.X
                else -> return@bind
            }
            when(stakeOption) {
                is ItemStakeOption.Amount -> {
                    duel.removeItem(itemId, stakeOption.amount)
                }
                ItemStakeOption.X -> {
                    player.sendInputInt("Enter amount:") { amountEntered: Int ->

                        val amount = amountEntered.coerceAtMost(myContainer.getAmountOf(itemId))
                        if (amount <= 0)
                            return@sendInputInt

                        duel.removeItem(itemId, amount)
                    }
                }
            }
        }
    }

    override fun getInterface() = GameInterface.DUEL_STAKING

}
