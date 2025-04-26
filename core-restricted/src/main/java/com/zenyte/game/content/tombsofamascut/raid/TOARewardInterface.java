package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.dialogue.OptionDialogue;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Savions
 */
public class TOARewardInterface extends Interface {

    @Override
    protected void attach() {
        put(4, "Bank-all");
        put(6, "Inventory-all");
        put(8, "Discard-all");
        put(10, "Take-x");
    }

    @Override
    protected void build() {
        bind("Bank-all", (player, slotId, itemId, option) -> {
            final Container container = player.getTOAManager().getRewardContainer();
            if (container == null || container.isEmpty()) {
                player.sendMessage("There is nothing to bank.");
            } else {
                final int size = container.getSize();
                for (int i = 0; i < size; i++) {
                    if (container.getSize() < 1) {
                        return;
                    }
                    final Item item = container.get(0);
                    final int currentAmount = player.getBank().getAmountOf(item.getId());
                    player.getBank().add(item);
                    final int newAmount = Math.max(0, item.getAmount() - (player.getBank().getAmountOf(item.getId()) - currentAmount));
                    if (newAmount > 0) {
                        item.setAmount(newAmount);
                    } else {
                        container.set(0, null);
                        container.shift();
                    }
                }
                checkSize(player, container);
                player.getPacketDispatcher().sendUpdateItemContainer(container);
            }
        });
        bind("Inventory-all", (player, slotId, itemId, option) -> {
            final Container container = player.getTOAManager().getRewardContainer();
            if (container == null || container.isEmpty()) {
                player.sendMessage("There is nothing to put in your inventory.");
            } else {
                final int size = container.getSize();
                for (int i = 0; i < size; i++) {
                    if (container.getSize() < 1) {
                        return;
                    }
                    final Item item = container.get(0);
                    final int currentAmount = player.getInventory().getAmountOf(item.getId());
                    player.getInventory().addItem(item);
                    final int newAmount = Math.max(0, item.getAmount() - (player.getInventory().getAmountOf(item.getId()) - currentAmount));
                    if (newAmount > 0) {
                        item.setAmount(newAmount);
                    } else {
                        container.set(0, null);
                        container.shift();
                    }
                }
                checkSize(player, container);
                player.getPacketDispatcher().sendUpdateItemContainer(container);
            }
        });
        bind("Discard-all", (player, slotId, itemId, option) -> {
            final Container container = player.getTOAManager().getRewardContainer();
            if (container == null || container.isEmpty()) {
                player.sendMessage("There is nothing to discard.");
            } else {
                player.getDialogueManager().start(new OptionDialogue(player, "Are you sure you want to discard everything?", new String[] {"No.", "Yes."}, new Runnable[] {() -> getInterface().open(player), () -> {
                    container.clear();
                    getInterface().open(player);
                    player.getPacketDispatcher().sendUpdateItemContainer(container);
                    checkSize(player, container);
                }}));
            }
        });
        bind("Take-x", (player, slotId, itemId, option) -> {
            final Container container = player.getTOAManager().getRewardContainer();
            if (container == null || container.isEmpty() || slotId >= container.getSize()) {
                player.getInterfaceHandler().closeInterfaces();
            } else {
                final Item containerItem = container.get(slotId);
                int amountToTake = 1;
                if (option == 2) {
                    amountToTake = 5;
                } else if (option == 3) {
                    amountToTake = 10;
                } else if (option == 4) {
                    amountToTake = containerItem.getAmount();
                }
                final int availableAmount = containerItem.getAmount();
                amountToTake = Math.min(amountToTake, availableAmount);
                player.getInventory().addItem(new Item(containerItem.getId(), amountToTake));
                final int newAmount = Math.max(0, availableAmount - amountToTake);
                if (newAmount > 0) {
                    containerItem.setAmount(newAmount);
                } else {
                    container.set(slotId, null);
                    container.shift();
                }
                player.getPacketDispatcher().sendUpdateItemContainer(container);
                checkSize(player, container);
            }
        });
    }

    private void checkSize(Player player, Container container) {
        if (container.getSize() < 1) {
            player.getVarManager().sendBit(14319, 0);
        }
    }

    @Override
    public void open(Player player) {
        if (player.getTOAManager().getRewardContainer() != null && !player.getTOAManager().getRewardContainer().isEmpty()) {
            player.getTOAManager().getRewardContainer().setContainerSize(6);
            player.getPacketDispatcher().sendUpdateItemContainer(player.getTOAManager().getRewardContainer());
        }
        super.open(player);
        player.getPacketDispatcher().sendClientScript(149, 50528266, 811, 2, 3, 0, -1, "Take", "Take-5", "Take-10", "Take-All", "");
        player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), 10, 0, 5, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP10);
    }


    @Override
    public GameInterface getInterface() {
        return GameInterface.TOA_LOOT;
    }
}
