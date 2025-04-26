package com.zenyte.plugins.renewednpc;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

/**
 * @author Kris | 12/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Hezaff extends NPCPlugin {

    @Override
    public void handle() {
        bind("Buy-teletabs", (player, npc) -> {
            final int purchases = player.getVariables().getTeletabPurchases();
            if (purchases >= 1000) {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You've already purchased the " +
                        "daily maximum of 1,000 teletabs from me!"));
                return;
            }
            final Inventory inventory = player.getInventory();
            if (!inventory.hasFreeSlots() && !inventory.containsItem(CustomItemId.NR_TABLET, 1)) {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You need some free inventory space to purchase these!"));
                return;
            }
            final int price = 175;
            final int coins = inventory.getAmountOf(ItemId.COINS_995);
            final int maxAmountPurchaseable = coins / price;
            final int count = inventory.getAmountOf(CustomItemId.NR_TABLET);
            final int maxAvailablePurchase = Math.min(Math.min(maxAmountPurchaseable, 1000 - purchases), Integer.MAX_VALUE - count);
            if (maxAvailablePurchase <= 0) {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You can't afford any teletabs!"));
                return;
            }
            player.sendInputInt("How many would you like to purchase for " + price + " each? (0-" + maxAvailablePurchase + ")", value -> {
                final int currentCoins = inventory.getAmountOf(ItemId.COINS_995);
                final int currentMaxAmountPurchaseable = currentCoins / price;
                final int currentCount = inventory.getAmountOf(CustomItemId.NR_TABLET);
                final int currentMaxAvailablePurchase = Math.min(Math.min(currentMaxAmountPurchaseable, 1000 - purchases), Integer.MAX_VALUE - currentCount);
                final int purchaseQuantity = Math.min(value, currentMaxAvailablePurchase);
                if (purchaseQuantity <= 0) {
                    return;
                }
                player.getVariables().setTeletabPurchases(purchases + purchaseQuantity);
                inventory.deleteItem(new Item(ItemId.COINS_995, purchaseQuantity * price));
                inventory.addOrDrop(new Item(CustomItemId.NR_TABLET, purchaseQuantity));
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "Pleasure doing business with you!"));
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {10006};
    }
}
