package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 04/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SackInteraction extends ItemPlugin implements PairedItemOnItemPlugin {

    private enum SackType {
        POTATOES(ItemId.POTATOES1, ItemId.POTATO), ONIONS(ItemId.ONIONS1, ItemId.ONION), CABBAGES(ItemId.CABBAGES1, ItemId.CABBAGE);
        private final int baseSackId;
        private final int vegetableId;
        private static final List<SackType> values = Collections.unmodifiableList(Arrays.asList(values()));

        private final int amountInside(final int sackId) {
            return 1 + ((sackId - baseSackId) / 2);
        }

        private final boolean isOfType(final int sackId) {
            final int containedAmount = amountInside(sackId);
            return containedAmount >= 1 && containedAmount <= 10;
        }

        SackType(int baseSackId, int vegetableId) {
            this.baseSackId = baseSackId;
            this.vegetableId = vegetableId;
        }
    }

    @Override
    public void handle() {
        bind("Fill", (player, item, container, slotId) -> {
            final int sackId = item.getId();
            final Inventory inventory = player.getInventory();
            final SackInteraction.SackType matchingType = CollectionUtils.findMatching(SackType.values, value -> value.isOfType(sackId));
            final int amountCarried = matchingType == null ? 0 : matchingType.amountInside(sackId);
            if (amountCarried >= 10) {
                player.sendMessage("The " + ItemDefinitions.getOrThrow(Objects.requireNonNull(matchingType).vegetableId).getName().toLowerCase() + " sack is already full.");
                return;
            }
            //The vegetable priority order is following: If the sack already contains one or more, we know the type. If not, we check cabbages, then onions and lastly potatoes.
            final int vegetableId = matchingType != null ? matchingType.vegetableId : (inventory.containsItem(SackType.CABBAGES.vegetableId, 1) ? SackType.CABBAGES.vegetableId : inventory.containsItem(SackType.ONIONS.vegetableId, 1) ? SackType.ONIONS.vegetableId : inventory.containsItem(SackType.POTATOES.vegetableId, 1) ? SackType.POTATOES.vegetableId : -1);
            if (vegetableId == -1) {
                player.sendMessage("You don't have any potatoes, onions or cabbages.");
                return;
            }
            final int vegetablesInInventory = inventory.getAmountOf(vegetableId);
            if (vegetablesInInventory <= 0) {
                player.sendMessage("You don't have any " + ItemDefinitions.getOrThrow(vegetableId).getName().toLowerCase() + (matchingType == SackType.POTATOES ? "e" : "") + "s.");
                return;
            }
            final int amountToAdd = Math.min(10 - amountCarried, vegetablesInInventory);
            if (amountToAdd <= 0) {
                return;
            }
            inventory.deleteItem(vegetableId, amountToAdd);
            item.setId(item.getId() + (amountToAdd * 2));
            inventory.refreshAll();
        });
        bind("Remove-one", (player, item, container, slotId) -> {
            final Inventory inventory = player.getInventory();
            if (!inventory.hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to do this.");
                return;
            }
            final int sackId = item.getId();
            final SackInteraction.SackType matchingType = Objects.requireNonNull(CollectionUtils.findMatching(SackType.values, value -> value.isOfType(sackId)));
            inventory.addOrDrop(new Item(matchingType.vegetableId));
            item.setId(item.getId() == matchingType.baseSackId ? ItemId.EMPTY_SACK : (item.getId() - 2));
            inventory.refreshAll();
            final String vegetableName = ItemDefinitions.getOrThrow(matchingType.vegetableId).getName().toLowerCase();
            player.sendFilteredMessage("You take a " + vegetableName + " out of the " + vegetableName + " sack.");
        });
        bind("Empty", (player, item, container, slotId) -> {
            final Inventory inventory = player.getInventory();
            final int freeSlots = inventory.getFreeSlots();
            if (freeSlots <= 0) {
                player.sendMessage("You need some free inventory space to do this.");
                return;
            }
            final int sackId = item.getId();
            final SackInteraction.SackType matchingType = Objects.requireNonNull(CollectionUtils.findMatching(SackType.values, value -> value.isOfType(sackId)));
            final int amountToRemove = Math.min(freeSlots, matchingType.amountInside(sackId));
            inventory.addOrDrop(new Item(matchingType.vegetableId, amountToRemove));
            final int nextId = item.getId() - (2 * amountToRemove);
            item.setId(nextId < matchingType.baseSackId ? ItemId.EMPTY_SACK : nextId);
            inventory.refreshAll();
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final int fromId = from.getId();
        final Item vegetable = fromId == SackType.POTATOES.vegetableId || fromId == SackType.ONIONS.vegetableId || fromId == SackType.CABBAGES.vegetableId ? from : to;
        final Item sack = vegetable == from ? to : from;
        final SackInteraction.SackType matchingSack = CollectionUtils.findMatching(SackType.values, value -> value.isOfType(sack.getId()));
        final SackInteraction.SackType sackType = Objects.requireNonNull(CollectionUtils.findMatching(SackType.values, value -> value.vegetableId == vegetable.getId()));
        final String vegetableName = vegetable.getName().toLowerCase();
        //The pairings should not allow this but if someone does happen to change it, make sure we can't add different vegetables in different sacks.
        if (matchingSack != null && !Objects.equals(matchingSack, sackType)) {
            throw new IllegalStateException();
        }
        final Inventory inventory = player.getInventory();
        if (matchingSack == null) {
            inventory.deleteItem(vegetable);
            sack.setId(sackType.baseSackId);
            inventory.refreshAll();
            player.sendFilteredMessage("You put " + Utils.getAOrAn(vegetableName) + " " + vegetableName + " in the vegetable sack.");
        } else {
            if (sackType.amountInside(sack.getId()) >= 10) {
                player.sendMessage("The " + vegetableName + " sack is already full!");
                return;
            }
            inventory.deleteItem(vegetable);
            sack.setId(sack.getId() + 2);
            inventory.refreshAll();
            player.sendFilteredMessage("You put " + Utils.getAOrAn(vegetableName) + " " + vegetableName + " in the " + vegetableName + " sack.");
        }
    }

    @Override
    public int[] getItems() {
        final IntArrayList intList = new IntArrayList();
        for (final SackInteraction.SackType type : SackType.values) {
            for (int i = 0; i < 10; i++) {
                intList.add(type.baseSackId + (i * 2));
            }
        }
        intList.add(ItemId.EMPTY_SACK);
        return intList.toIntArray();
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final SackInteraction.SackType type : SackType.values) {
            pairs.add(ItemPair.of(type.vegetableId, ItemId.EMPTY_SACK));
            for (int i = 0; i < 10; i++) {
                pairs.add(ItemPair.of(type.vegetableId, type.baseSackId + (i * 2)));
            }
        }
        return pairs.toArray(new ItemPair[0]);
    }
}
