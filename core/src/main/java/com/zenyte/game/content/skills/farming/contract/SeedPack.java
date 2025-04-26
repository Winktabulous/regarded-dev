package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Christopher
 * @since 4/8/2020
 */
public class SeedPack extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", ((player, seedPack, slotId) -> {
            final int highRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_HIGH_ROLLS_ATTR).intValue();
            final int mediumRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_MEDIUM_ROLLS_ATTR).intValue();
            final int lowRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_LOW_ROLLS_ATTR).intValue();
            final int total = highRolls + mediumRolls + lowRolls;
            final String totalString = total > 1 ? total + " sets" : total + " set";
            player.sendMessage("The packet contains " + totalString + " of seeds.");
        }));
        bind("Take", (player, seedPack, slotId) -> {
            final int highRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_HIGH_ROLLS_ATTR).intValue();
            final int mediumRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_MEDIUM_ROLLS_ATTR).intValue();
            final int lowRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_LOW_ROLLS_ATTR).intValue();
            final Inventory inventory = player.getInventory();
            if (highRolls > 0) {
                inventory.addOrDrop(HighSeedTable.roll());
                seedPack.setAttribute(FarmingContract.SEED_PACK_HIGH_ROLLS_ATTR, highRolls - 1);
            } else if (mediumRolls > 0) {
                inventory.addOrDrop(MediumSeedTable.roll());
                seedPack.setAttribute(FarmingContract.SEED_PACK_MEDIUM_ROLLS_ATTR, mediumRolls - 1);
            } else if (lowRolls > 0) {
                inventory.addOrDrop(LowSeedTable.roll());
                seedPack.setAttribute(FarmingContract.SEED_PACK_LOW_ROLLS_ATTR, lowRolls - 1);
            }
            deleteIfEmpty(player, seedPack, slotId);
        });
    }

    private void deleteIfEmpty(final Player player, final Item seedPack, final int slot) {
        final int highRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_HIGH_ROLLS_ATTR).intValue();
        final int mediumRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_MEDIUM_ROLLS_ATTR).intValue();
        final int lowRolls = seedPack.getNumericAttribute(FarmingContract.SEED_PACK_LOW_ROLLS_ATTR).intValue();
        if (!hasRemainingRolls(lowRolls, mediumRolls, highRolls)) {
            player.getInventory().deleteItem(slot, seedPack);
        }
    }

    private boolean hasRemainingRolls(final int lowRolls, final int mediumRolls, final int highRolls) {
        return lowRolls > 0 || mediumRolls > 0 || highRolls > 0;
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.SEED_PACK};
    }
}
