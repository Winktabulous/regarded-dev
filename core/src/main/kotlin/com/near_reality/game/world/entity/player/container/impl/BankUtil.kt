package com.near_reality.game.world.entity.player.container.impl

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.bank.Bank

/**
 * Utility class for managing bank actions.
 *
 * @author Stan van der Bend
 */
object BankUtil {

    @JvmStatic
    fun safeDeposit(player: Player, container: Container) =
        safeDeposit(player.bank, player, container)

    @JvmStatic
    fun safeDeposit(bank: Bank, player: Player, container: Container) =
        safePermutation(bank, player) {
            for (slot in 0 until container.containerSize) {
                val item = container[slot] ?: continue
                player.bank.deposit(null, container, slot, item.amount)
            }
        }


    @JvmStatic
    fun safePermutation(player: Player, block: (Bank) -> Unit) =
        safePermutation(player.bank, player, block)

    @JvmStatic
    fun <T : Bank> safePermutation(bank: T, player: Player, block: (T) -> Unit) {
        acquirePermutationAccessIfMultiBank(bank, player)
        try {
            block(bank)
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            relinquishPermutationAccessIfMultiBank(bank, player)
        }
    }

    @JvmStatic
    private fun <T : Bank> acquirePermutationAccessIfMultiBank(bank: T, player: Player) {
        if (bank is MultiPlayerBank)
            bank.acquirePermutationAccess(player)
    }

    @JvmStatic
    private fun <T : Bank> relinquishPermutationAccessIfMultiBank(bank: T, player: Player) {
        if (bank is MultiPlayerBank)
            bank.relinquishPermutationAccess(player)
    }
}
