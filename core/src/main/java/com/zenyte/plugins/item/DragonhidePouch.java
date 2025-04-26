package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;


public class DragonhidePouch extends ItemPlugin {

    public static final Item OPENED = new Item(26302);
    public static final Item CLOSED = new Item(26300);

    @Override
    public void handle() {
        bind("Fill", (player, item, slotId) -> player.getDragonhidePouch().fill());
        bind("Check", (player, item, slotId) -> player.getDragonhidePouch().check());
        bind("Empty", (player, item, slotId) -> player.getDragonhidePouch().empty(player.getInventory().getContainer()));
        bind("Open", (player, item, slotId) -> {
            player.getDragonhidePouch().setOpen(true);
            player.getInventory().set(slotId, OPENED);
            player.sendMessage("You open your dragonhide pouch, ready to fill it.");
        });
        bind("Close", (player, item, slotId) -> {
            player.getDragonhidePouch().setOpen(false);
            player.getInventory().set(slotId, CLOSED);
            player.sendMessage("You close your dragonhide pouch.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 26302, 26300 };
    }
}
