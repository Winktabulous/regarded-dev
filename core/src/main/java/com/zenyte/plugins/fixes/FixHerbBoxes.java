package com.zenyte.plugins.fixes;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.bank.Bank;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.PostInitializationEvent;

public class FixHerbBoxes {

    @Subscribe
    public static void onLoginEvent(PostInitializationEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            Bank bank = player.getBank();
            if(bank != null && bank.containsItem(new Item(ItemId.HERB_BOX, 500))) {
                int herbBoxCount = bank.getAmountOf(ItemId.HERB_BOX);
                if(herbBoxCount > 0)
                    bank.remove(new Item(ItemId.HERB_BOX, herbBoxCount));
            }
            if(bank != null && bank.containsItem(new Item(ItemId.OPENED_HERB_BOX, 500))) {
                int herbBoxCount = bank.getAmountOf(ItemId.OPENED_HERB_BOX);
                if(herbBoxCount > 0)
                    bank.remove(new Item(ItemId.OPENED_HERB_BOX, herbBoxCount));
            }


            Inventory inventory = player.getInventory();
            if(inventory != null && inventory.containsItem(new Item(ItemId.HERB_BOX, 500))) {
                int herbBoxCount = inventory.getAmountOf(ItemId.HERB_BOX);
                if(herbBoxCount > 0)
                    inventory.deleteItem(new Item(ItemId.HERB_BOX, herbBoxCount));
            }
            if(inventory != null && inventory.containsItem(new Item(ItemId.OPENED_HERB_BOX, 500))) {
                int herbBoxCount = inventory.getAmountOf(ItemId.OPENED_HERB_BOX);
                if(herbBoxCount > 0)
                    inventory.deleteItem(new Item(ItemId.OPENED_HERB_BOX, herbBoxCount));
            }

        }
    }
}
