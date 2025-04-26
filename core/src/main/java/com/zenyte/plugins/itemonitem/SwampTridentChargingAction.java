package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.trident.SwampTrident;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * @author Christopher
 * @since 2/20/2020
 */
public final class SwampTridentChargingAction implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item tridentItem = SwampTrident.allTridentsMap.keySet().contains(from.getId()) ? from : to;
        final SwampTrident tridentDef = SwampTrident.allTridentsMap.get(tridentItem.getId());
        final int tridentSlot = from == tridentItem ? fromSlot : toSlot;
        tridentDef.charge(player, tridentItem, tridentSlot);
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectOpenHashSet<ItemOnItemAction.ItemPair> pairs = new ObjectOpenHashSet<ItemPair>();
        for (Item rune : SwampTrident.chargingMaterials) {
            for (Integer tridentId : SwampTrident.allTridentsMap.keySet()) {
                pairs.add(ItemPair.of(rune.getId(), tridentId));
            }
        }
        return pairs.toArray(new ItemPair[0]);
    }
}
