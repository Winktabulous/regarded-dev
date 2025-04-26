package com.zenyte.game.content.consumables;

import com.near_reality.game.content.consumables.drinks.DivinePotion;
import com.zenyte.game.content.consumables.drinks.BarbarianMix;
import com.zenyte.game.content.consumables.drinks.GourdPotion;
import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Kris | 04/12/2018 19:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Decantation implements ItemOnItemAction {
    private static final int vial = 229;
    private static final int gourdVial = 20800;

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (from.getId() == vial || to.getId() == vial || from.getId() == gourdVial || to.getId() == gourdVial || from.getId() == ItemId.EMPTY_CUP || to.getId() == ItemId.EMPTY_CUP) {
            final Item vial = from.getId() == Decantation.vial || from.getId() == gourdVial || from.getId() == ItemId.EMPTY_CUP ? from : to;
            final Item potion = vial == from ? to : from;
            final Consumable consumable = Consumable.consumables.get(potion.getId());
            if (!(consumable instanceof Drinkable)) return;
            final Drinkable drink = (Drinkable) consumable;
            final int doses = drink.getDoses(potion.getId());
            final Item vialReplacement = drink.getItem((int) Math.ceil(doses / 2.0F));
            final Item potionReplacement = drink.getItem(doses / 2);
            final Inventory inventory = player.getInventory();
            inventory.set(vial == from ? fromSlot : toSlot, vialReplacement);
            inventory.set(potion == from ? fromSlot : toSlot, potionReplacement);
            inventory.refresh();
            player.sendMessage("You divide the liquid between the vessels.");
            return;
        }
        final Consumable fromConsumable = Consumable.consumables.get(from.getId());
        if (!(fromConsumable instanceof Drinkable)) return;
        final Consumable toConsumable = Consumable.consumables.get(to.getId());
        if (!(toConsumable instanceof Drinkable)) return;
        final Drinkable fromDrink = (Drinkable) fromConsumable;
        final Drinkable toDrink = (Drinkable) toConsumable;
        final int fromDoses = fromDrink.getDoses(from.getId());
        final int toDoses = toDrink.getDoses(to.getId());
        final int toLength = toDrink.getIds().length;
        if (toDoses == toLength) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        final int decantedToDoses = Math.min(toLength, toDoses + fromDoses);
        final int decantedFromDoses = fromDoses - (decantedToDoses - toDoses);
        final Inventory inventory = player.getInventory();
        final Item fromReplacement = fromDrink.getItem(decantedFromDoses);
        inventory.set(fromSlot, (fromReplacement == null && fromDrink instanceof GourdPotion) ? new Item(gourdVial) : (fromReplacement == null && fromDrink == Potion.GUTHIX_REST) ? new Item(ItemId.EMPTY_CUP) : fromReplacement);
        inventory.set(toSlot, toDrink.getItem(decantedToDoses));
        inventory.refresh();
        player.sendMessage("You have combined the liquid into " + decantedToDoses + " doses.");
    }

    public ItemPair[] getMatchingPairs() {
        final ArrayList<ItemOnItemAction.ItemPair> pairs = new ArrayList<ItemPair>();
        final ArrayList<Drinkable> decantablePotions = new ArrayList<Drinkable>(1500);
        decantablePotions.addAll(Arrays.asList(Potion.values));
        decantablePotions.addAll(Arrays.asList(BarbarianMix.values));
        decantablePotions.addAll(Arrays.asList(GourdPotion.values));
        decantablePotions.addAll(Arrays.asList(DivinePotion.Companion.getAll()));

        for (final Drinkable drinkable : decantablePotions) {
            final boolean isGourdVial = drinkable instanceof GourdPotion;
            final int[] ids = drinkable.getIds();
            int i;
            int j;
            for (i = 0; i < ids.length; i++) {
                int id = ids[i];
                for (j = 0; j < ids.length; j++) {
                    int secondId = ids[j];
                    pairs.add(ItemPair.of(id, secondId));
                }
                if (i != 0) {
                    pairs.add(ItemPair.of(drinkable == Potion.GUTHIX_REST ? ItemId.EMPTY_CUP : isGourdVial ? gourdVial : vial, id));
                }
            }
        }
        return pairs.toArray(new ItemPair[0]);
    }

    @Override
    public int[] getItems() {
        return new int[0];
    }
}
