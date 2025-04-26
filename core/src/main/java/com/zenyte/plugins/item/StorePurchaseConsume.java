package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.RequestResult;

public class StorePurchaseConsume extends ItemPlugin {
    @Override
    public void handle() {
        bind("Consume", (player, item, slotId) -> {
            int itemId = item.getId();
            ContainerResult result = player.getInventory().deleteItem(item.getId(), 1);
            if(result.getResult() == RequestResult.SUCCESS) {
                Item winnerWinner = lookupItem(itemId);
                if(winnerWinner != null)
                    player.getInventory().addItem(winnerWinner);
            }
        });
    }

    private Item lookupItem(int itemId) {
        switch(itemId) {
            case 31300: return new Item(19677, 6);
            case 31301: return new Item(2, 1000);
            case 31302: return new Item(6686, 60).toNote();
            case 31303: return new Item(3025, 60).toNote();
            case 31304: return new Item(12696, 60).toNote();
            case 31305: return new Item(3145, 250).toNote();
            case 31306: return new Item(12626, 60).toNote();
            case 31307: return new Item(13442, 150).toNote();
            case 31308: return new Item(398, 200).toNote();
            case 31309: return new Item(22450, 60).toNote();
            case 31310: return new Item(22462, 60).toNote();
            case 31311: return new Item(20718, 100);
            case 31312: return new Item(11959, 150);
            case 31313: return new Item(537, 100);
            case 31314: return new Item(21728, 1000);
            case 31315: return new Item(30210, 10);
            case 31316: return new Item(30210, 100);
            case 31317: return new Item(32158, 25);
        }
        return null;
    }

    @Override
    public int[] getItems() {
        return new int[]{31300, 31301, 31302, 31303, 31304, 31305, 31306, 31307, 31308, 31309, 31310, 31311, 31312, 31313, 31314, 31315, 31316, 31317};
    }
}
