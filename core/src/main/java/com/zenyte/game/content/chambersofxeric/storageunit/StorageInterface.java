package com.zenyte.game.content.chambersofxeric.storageunit;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 23/09/2019 17:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class StorageInterface extends Interface {

    /**
     * Handles the interactions with the item in the storage unit, at the specified slot.
     *
     * @param player  the player interacting with the storage unit.
     * @param storage the storage unit being interacted with.
     * @param option  the id of the option clicked on the item in the storage.
     * @param slot    the slot id of the item in the storage unit.
     * @param item    the item object in the specified slot in the storage.
     */
    void handleInteraction(@NotNull final Player player, @NotNull final Storage storage, final int option, final int slot, @NotNull final Item item) {
        switch (option) {
            case 1:
                storage.withdraw(player, slot, 1, true);
                break;
            case 2:
                storage.withdraw(player, slot, 5, true);
                break;
            case 3:
                storage.withdraw(player, slot, 10, true);
                break;
            case 4:
                storage.withdraw(player, slot, Integer.MAX_VALUE, true);
                break;
            case 5:
                player.sendInputInt("Enter amount:", amount -> storage.withdraw(player, slot, amount, true));
                break;
            case 10:
                ItemUtil.sendItemExamine(player, item);
        }
    }

}
