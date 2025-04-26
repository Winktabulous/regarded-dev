package com.near_reality.game.world.entity.player.container.impl

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerResult
import com.zenyte.game.world.entity.player.container.impl.bank.Bank
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting
import java.util.function.Predicate

/**
 * Represents a [Bank] that can be shared by multiple [players].
 *
 * @author Stan van der Bend
 */
class MultiPlayerBank : Bank() {

    @Transient
    @kotlinx.serialization.Transient
    private val players = mutableSetOf<Player>()

    /**
     * Represents the [Player] making a bank modification, or `null` if no-one is, this is a temporary variable.
     *
     * It is set to the [Player] performing a container action before the action is performed
     * and set to `null` immediately after the container action is performed.
     *
     * This is a backing field for the non-nullable property [tempPlayerModifyingBank].
     */
    @Volatile
    @Transient
    @kotlinx.serialization.Transient
    private var _tempPlayerModifyingBank: Player? = null

    /**
     * Returns the [_tempPlayerModifyingBank] if not `null` or else throws an exception.
     */
    private val tempPlayerModifyingBank: Player
        get() = requireNotNull(_tempPlayerModifyingBank) { "No accessing player set" }

    override fun onOpen(player: Player) {
        if (players.contains(player)) {
            player.sendDeveloperMessage("Attempted to add yourself twice to MultiPlayerBank.players, see logs for stacktrace")
            Thread.dumpStack()
        }
        players += player
    }

    override fun onClose(player: Player) {
        if (!players.contains(player)) {
            player.sendDeveloperMessage("Attempted to remove yourself but you're not in MultiPlayerBank.players, see logs for stacktrace")
            Thread.dumpStack()
        }
        players -= player
    }

    override fun refreshQuantity() {
        players.forEach {
            it.varManager.sendBit(VARBIT_FIRST_OP_AMOUNT, quantity.value)
        }
    }

    override fun testAddPredicate(predicate: Predicate<Player>): Boolean =
        predicate.test(tempPlayerModifyingBank)

    override fun sendMessage(string: String) {
        players.forEach {
            it.sendMessage(string)
        }
    }

    override fun removeItemDelegate(item: Item, placeholder: Boolean): ContainerResult =
        container.remove(item, placeholder, tempPlayerModifyingBank)

    override fun requestContainerRefresh(container: Container) {
        players.forEach(container::refresh)
    }

    override fun updateVar(setting: BankSetting) {
        players.forEach(setting::updateVar)
    }

    override fun refreshTabSizes() {
        players.forEach(this::refreshBankSizes)
    }

    internal fun acquirePermutationAccess(player: Player) {
        _tempPlayerModifyingBank = player
        container.linkPlayer(player)
        player.sendDeveloperMessage("Acquired temp permutation access to bank")
    }

    internal fun relinquishPermutationAccess(player: Player) {
        if (_tempPlayerModifyingBank != player)
            player.sendDeveloperMessage("Could not relinquish access because you do not have it, $_tempPlayerModifyingBank has.")
        else {
            _tempPlayerModifyingBank = null
            container.unlinkPlayer()
            player.sendDeveloperMessage("Relinquish temp permutation access to bank")
        }
    }
}
