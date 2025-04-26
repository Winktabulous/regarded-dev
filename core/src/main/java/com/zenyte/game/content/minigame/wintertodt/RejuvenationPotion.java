package com.zenyte.game.content.minigame.wintertodt;

import com.google.common.primitives.Ints;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author Corey
 * @since 23:53 - 21/07/2019
 */
public class RejuvenationPotion extends ItemPlugin implements ItemOnItemAction {
    static final int BRUMA_HERB = 20698;
    private static final float HERBLORE_EXP = 3.5F;

    static Item getNextPotion(final int potion) {
        if (potion == Potion.FOUR_DOSE.getItemId()) {
            return new Item(Potion.THREE_DOSE.getItemId());
        } else if (potion == Potion.THREE_DOSE.getItemId()) {
            return new Item(Potion.TWO_DOSE.getItemId());
        } else if (potion == Potion.TWO_DOSE.getItemId()) {
            return new Item(Potion.ONE_DOSE.getItemId());
        } else {
            return null;
        }
    }

    @Override
    public void handle() {
        setDefault("Drop", (player, item, slotId) -> {
            final String itemName = Potion.finishedPotions.contains(item.getId()) ? "potion" : "vial";
            player.sendMessage("The " + itemName + " shatters as it hits the floor.");
            player.getInventory().deleteItem(slotId, item);
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item potion = from.getId() == Potion.UNFINISHED.getItemId() ? from : to;
        final Item herb = potion == from ? to : from;
        if (potion.getId() != Potion.UNFINISHED.getItemId() || herb.getId() != BRUMA_HERB) {
            return;
        }
        player.getActionManager().setAction(new MakePotionAction(fromSlot, toSlot));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {new ItemPair(Potion.UNFINISHED.getItemId(), BRUMA_HERB)};
    }

    @Override
    public int[] getItems() {
        return Ints.toArray(Potion.items);
    }


    enum Potion {
        UNFINISHED(20697), ONE_DOSE(20702), TWO_DOSE(20701), THREE_DOSE(20700), FOUR_DOSE(20699);
        static final Potion[] values = values();
        static final IntSet items = new IntArraySet(values.length);
        static final IntSet finishedPotions = new IntArraySet(values.length);

        static {
            for (final RejuvenationPotion.Potion potion : values) {
                items.add(potion.getItemId());
                if (potion != UNFINISHED) {
                    finishedPotions.add(potion.getItemId());
                }
            }
        }

        private final int itemId;

        Potion(int itemId) {
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }
    }


    private static final class MakePotionAction extends Action {
        static final Item herb = new Item(BRUMA_HERB);
        static final Item unfinishedPotion = new Item(Potion.UNFINISHED.getItemId());
        private final int fromSlot;
        private final int toSlot;

        @Override
        public boolean start() {
            player.getInventory().set(fromSlot, null);
            player.getInventory().set(toSlot, new Item(Potion.FOUR_DOSE.getItemId()));
            player.getSkills().addXp(SkillConstants.HERBLORE, HERBLORE_EXP);
            player.sendMessage("You combine the bruma herb into the unfinished potion.");
            delay(2);
            return true;
        }

        @Override
        public boolean process() {
            return player.getInventory().containsItems(herb, unfinishedPotion);
        }

        @Override
        public int processWithDelay() {
            player.getInventory().deleteItems(herb, unfinishedPotion);
            player.getInventory().addItem(Potion.FOUR_DOSE.getItemId(), 1);
            player.getSkills().addXp(SkillConstants.HERBLORE, HERBLORE_EXP);
            player.sendMessage("You combine the bruma herb into the unfinished potion.");
            return 1;
        }

        @Override
        public void stop() {
        }

        public MakePotionAction(int fromSlot, int toSlot) {
            this.fromSlot = fromSlot;
            this.toSlot = toSlot;
        }
    }
}
