package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Tommeh | 2-1-2019 | 17:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SeedBox extends ItemPlugin {

    public static final Item OPENED = new Item(ItemId.OPEN_SEED_BOX);
    public static final Item CLOSED = new Item(ItemId.SEED_BOX);

    @Override
    public void handle() {
        bind("Fill", (player, item, slotId) -> player.getSeedBox().fill());
        bind("Check", (player, item, slotId) -> GameInterface.SEED_BOX.open(player));
        bind("Empty", (player, item, slotId) -> player.getSeedBox().empty());
        bind("Open", (player, item, slotId) -> {
            player.getSeedBox().setOpen(true);
            player.getInventory().set(slotId, OPENED);
            player.sendMessage("You open your seed box, ready to fill it.");
        });
        bind("Close", (player, item, slotId) -> {
            player.getSeedBox().setOpen(false);
            player.getInventory().set(slotId, CLOSED);
            player.sendMessage("You close your seed box.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.OPEN_SEED_BOX, ItemId.SEED_BOX };
    }
}
