package com.zenyte.game.content.skills.hunter.aerialfishing.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Cresinkel
 */

public class AerialFishCutting implements PairedItemOnItemPlugin {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        Item knifeItem = from.getId() == ItemId.KNIFE ? from : to;
        Item fishItem = knifeItem.getId() == from.getId() ? to : from;
        Inventory inven = player.getInventory();
        inven.deleteItem(fishItem);
        inven.addItem(ItemId.FISH_CHUNKS, 1);
        player.getSkills().addXp(SkillConstants.COOKING, getCookingXP(fishItem.getId()));
        player.sendFilteredMessage("You cut a " + fishItem.getName() + " into chunks.");
    }

    private double getCookingXP(int fishItemId) {
        switch (fishItemId) {
            case ItemId.BLUEGILL:
                return AerialFish.BLUEGILL.cookingXP;
            case ItemId.COMMON_TENCH:
                return AerialFish.COMMON_TENCH.cookingXP;
            case ItemId.MOTTLED_EEL:
                return AerialFish.MOTTLED_EEL.cookingXP;
            case ItemId.GREATER_SIREN:
                return AerialFish.GREATER_SIREN.cookingXP;
            default:
                return 1;
        }
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[]{
                ItemPair.of(ItemId.BLUEGILL, ItemId.KNIFE),
                ItemPair.of(ItemId.COMMON_TENCH, ItemId.KNIFE),
                ItemPair.of(ItemId.MOTTLED_EEL, ItemId.KNIFE),
                ItemPair.of(ItemId.GREATER_SIREN, ItemId.KNIFE),
        };
    }
}
