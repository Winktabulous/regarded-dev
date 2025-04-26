package com.zenyte.game.content.chambersofxeric.storageunit;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

/**
 * @author Kris | 21/07/2019 02:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface Storage {

    /**
     * The container backing this storage unit.
     * @return the container backing this storage unit.
     */
    Container getContainer();

    /**
     * Deposits the item in the specified slot from the player's inventory into the storage, up to the amount specified.
     *
     * @param player the player depositing the item.
     * @param slotId the slot id of the item in the player's inventory.
     * @param amount the maximum amount of item to be deposited.
     */
    void deposit(final Player player, final int slotId, final int amount);

    /**
     * Withdraws the item in the specified slot in the storage into the player's inventory, up to the amount specified.
     *
     * @param player       the player who is withdrawing the item.
     * @param slot         the slot id of the item in the storage.
     * @param amount       the maximum amount of the item to be withdrawn.
     * @param sendMessages whether or not to send messages about the inventory being full if it restricts them from withdrawing all of the requested quantity.
     */
    void withdraw(final Player player, final int slot, final int amount, final boolean sendMessages);

    /**
     * Refreshes the shared storage container for all the players viewing it,
     */
    void refresh();
}
