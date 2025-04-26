package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Andys1814
 */
public final class CelestialRing extends ItemPlugin implements PairedItemOnItemPlugin, ChargeExtension {

    @Override
    public void handle() {
        bind("charge", this::charge);

        bind("uncharge", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you want to uncharge your <col=00080>" + item.getName() + "</col>?", "Yes, I'm " +
                        "sure.", "No.").onOptionOne(() -> {
                    player.getInventory().addOrDrop(new Item(ItemId.STARDUST, item.getCharges()));
                    container.set(slotId, new Item(ItemId.CELESTIAL_RING_UNCHARGED));
                    setKey(5);
                });
                item(5, item, "Your <col=00080>" + item.getName() + "</col> was successfully uncharged.");
            }
        }));
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.CELESTIAL_RING_UNCHARGED, ItemId.CELESTIAL_RING };
    }

    private void charge(Player player, Item item, Container container, int slotId) {
        int stardust = player.getInventory().getAmountOf(ItemId.STARDUST);
        player.sendInputInt("How many charges would you like to add? (0 - " + stardust + ")", (amount) -> {
            if (amount <= 0 || amount > stardust) {
                player.sendMessage("Please enter a valid amount of charges.");
                return;
            }

            int amountRemaining = 10_000 - item.getCharges();
            int toCharge = Math.min(Math.min(amount, amountRemaining), player.getInventory().getAmountOf(ItemId.STARDUST));

            if (item.getId() == ItemId.CELESTIAL_RING_UNCHARGED) {
                item.setId(ItemId.CELESTIAL_RING);
                container.refresh(slotId);
            }

            item.setCharges(item.getCharges() + toCharge);
            player.getInventory().deleteItem(ItemId.STARDUST, toCharge);

            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(new Item(ItemId.CELESTIAL_RING), "You add " + toCharge + " charges to your Celestial ring.");
                }
            });
        });
    }

    @Override
    public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
        final int charges = item.getCharges();
        item.setCharges((charges - amount) <= 0 ? 10_000 : (charges - amount));
        if ((charges - amount) <= 0) {
            player.getEquipment().set(EquipmentSlot.RING.getSlot(), new Item(ItemId.CELESTIAL_RING_UNCHARGED));
            player.sendMessage(Colour.RS_PURPLE.wrap("Your Celestial Ring has completely degraded."));
        }
    }

    @Override
    public void checkCharges(final Player player, final Item item) {
        int charges = item.getCharges();
        player.sendMessage("Your " + item.getName() + " has " + charges + " charge" + (charges == 1 ? "" : "s") + ".");
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (from.getId() == ItemId.CELESTIAL_RING_UNCHARGED || from.getId() == ItemId.CELESTIAL_RING) {
            charge(player, from, player.getInventory().getContainer(), fromSlot);
        } else {
            charge(player, to, player.getInventory().getContainer(), toSlot);
        }

    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(ItemId.STARDUST, ItemId.CELESTIAL_RING_UNCHARGED),
                ItemPair.of(ItemId.STARDUST, ItemId.CELESTIAL_RING)
        };
    }
}
