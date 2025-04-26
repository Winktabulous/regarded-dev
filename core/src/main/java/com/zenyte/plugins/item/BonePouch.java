package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;


public class BonePouch extends ItemPlugin {

    public static final Item OPENED = new Item(26306);
    public static final Item CLOSED = new Item(26304);

    @Override
    public void handle() {
        bind("Fill", (player, item, slotId) -> player.getBonePouch().fill());
        bind("Check", (player, item, slotId) -> player.getBonePouch().check());
        bind("Empty", (player, item, slotId) -> player.getBonePouch().empty(player.getInventory().getContainer()));
        bind("Open", (player, item, slotId) -> {
            player.getBonePouch().setOpen(true);
            player.getInventory().set(slotId, OPENED);
            player.sendMessage("You open your bone pouch, ready to fill it.");
        });
        bind("Close", (player, item, slotId) -> {
            player.getBonePouch().setOpen(false);
            player.getInventory().set(slotId, CLOSED);
            player.sendMessage("You close your bone pouch.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 26306, 26304 };
    }
}
