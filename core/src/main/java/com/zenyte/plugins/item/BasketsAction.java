package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.farming.BasketData;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.skills.FillBasketD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BasketsAction extends ItemPlugin {
    @Override
    public void handle() {
        bind("Fill", (player, item, slotId) -> {
            if (item.getId() == 5376) {
                final ArrayList<Item> items = new ArrayList<Item>();
                for (final BasketData basket : BasketData.VALUES) {
                    if (player.getInventory().containsItem(basket.getProduce())) items.add(basket.getFull());
                }
                if (items.size() == 0) {
                    player.sendMessage("You have nothing to fill the baskets with!");
                    return;
                }
                player.getDialogueManager().start(new FillBasketD(player, item, items.toArray(new Item[items.size()])));
            } else {
                final BasketData basket = BasketData.getBasket(item.getId());
                if (basket == null) return;
                if (item.getId() == basket.getFull().getId()) {
                    player.sendMessage("This basket is already full!");
                    return;
                }
                player.getDialogueManager().start(new FillBasketD(player, item, basket.getFull()));
            }
        });
        bind("Remove-one", (player, item, slotId) -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You don't have enough space in your inventory!");
                return;
            }
            final BasketData basket = BasketData.getBasket(item.getId());
            if (basket == null) return;
            player.getInventory().replaceItem((item.getId() - 2), 1, slotId);
            player.getInventory().addItem(basket.getProduce());
        });
        bind("Empty", (player, item, slotId) -> {
            final BasketData basket = BasketData.getBasket(item.getId());
            if (basket == null) return;
            final int space = (item.getId() - basket.getBasket().getId()) / 2;
            if (player.getInventory().getFreeSlots() < space + 1) {
                player.sendMessage("You don't have enough space in your inventory!");
                return;
            }
            player.getInventory().replaceItem(5376, 1, slotId);
            player.getInventory().addItem(basket.getProduce().getId(), space + 1);
        });
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (int key : BasketData.baskets.keySet()) {
            list.add(key);
        }
        list.rem(1982);
        list.rem(2108);
        list.rem(1963);
        list.rem(5504);
        list.add(5376);
        return list.toArray(new int[list.size()]);
    }
}
