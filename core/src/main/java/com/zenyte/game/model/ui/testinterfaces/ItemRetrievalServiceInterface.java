package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

import java.util.Optional;

/**
 * @author Kris | 01/11/2018 14:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemRetrievalServiceInterface extends Interface {
    @Override
    protected void attach() {
        put(3, "Items");
        put(8, "Discard-All");
        put(6, "Take-All/Unlock");
    }

    @Override
    public void open(Player player) {
        final ItemRetrievalService service = player.getRetrievalService();
        final ItemRetrievalService.RetrievalServiceType type = service.getType();
        if (type == null || service.getContainer().getSize() == 0) {
            player.sendMessage("You have no items to retrieve.");
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getVarManager().sendVar(ItemRetrievalService.TYPE_VAR, service.isLocked() ? type.getLockedEnumIndex() : type.getUnlockedEnumIndex());
        service.getContainer().setFullUpdate(true);
        service.getContainer().refresh(player);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Items"), 0, Container.getSize(ContainerType.ITEM_RETRIEVAL_SERVICE), AccessMask.CLICK_OP2, AccessMask.CLICK_OP9, AccessMask.CLICK_OP10);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        ItemRetrievalService.updateVarps(player);
    }

    @Override
    protected void build() {
        bind("Discard-All", player -> {
            player.getInterfaceHandler().closeInterface(getInterface());
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Are you sure you wish to discard all the items?", new DialogueOption("Yes, discard them all.", () -> {
                        final ItemRetrievalService service = player.getRetrievalService();
                        service.getContainer().clear();
                        service.setLocked(false);
                        service.setType(null);
                        player.sendMessage("All the contents of the retrieval service have been discarded.");
                    }), new DialogueOption("No, keep them.", () -> getInterface().open(player)));
                }
            });
        });
        bind("Take-All/Unlock", player -> {
            final ItemRetrievalService service = player.getRetrievalService();
            if (service.isLocked()) {
                final ItemRetrievalService.RetrievalServiceType type = service.getType();
                final int cost = type.getCost();
                final int inInventory = player.getInventory().getAmountOf(995);
                final int inBank = player.getBank().getAmountOf(995);
                final int total = inInventory + inBank;
                //If cost is above total and total is above 0(overflow results in two integer adding going negative value)
                if (cost > total && total >= 0) {
                    player.sendMessage("You need at least " + StringFormatUtil.format(cost) + " coins to unlock your retrieval service.");
                    return;
                }
                player.getInventory().deleteItem(995, cost).onFailure(remainingItem -> player.getBank().remove(remainingItem));
                service.setLocked(false);
                player.getVarManager().sendVar(ItemRetrievalService.TYPE_VAR, type.getUnlockedEnumIndex());
                player.sendMessage("You've unlocked the contents of the retrieval service.");
            } else {
                final Container container = service.getContainer();
                for (int slot = 0; slot < container.getContainerSize(); slot++) {
                    final Item item = container.get(slot);
                    if (item == null) {
                        continue;
                    }
                    player.getInventory().getContainer().deposit(null, container, slot, item.getAmount());
                }
                container.shift();
                container.refresh(player);
                player.getInventory().refresh();
                if (container.getSize() != 0) {
                    player.sendMessage("Not enough space in your inventory to reclaim all of the items.");
                    return;
                }
                player.getInterfaceHandler().closeInterface(getInterface().getPosition());
                player.sendMessage("You reclaim all of your items from the retrieval service.");
            }
        });
        bind("Items", (player, slotId, itemId, option) -> {
            final ItemRetrievalService service = player.getRetrievalService();
            final Container container = service.getContainer();
            final Item item = container.get(slotId);
            if (item == null) return;
            if (option == 2) {
                if (service.isLocked()) {
                    player.sendMessage("You need to unlock your retrieval service first.");
                    return;
                }
                player.getInventory().getContainer().deposit(player, container, slotId, container.getAmountOf(item.getId()));
                container.shift();
                container.refresh(player);
                player.getInventory().refresh();
                if (container.getSize() == 0) {
                    player.getInterfaceHandler().closeInterface(getInterface().getPosition());
                    player.sendMessage("You reclaim all of your items from the retrieval service.");
                }
            } else if (option == 9) {
                if (!item.isTradable()) {
                    player.sendMessage("This item is untradeable.");
                    return;
                }
                player.sendMessage("The value of " + item.getName() + " x " + item.getAmount() + " is " + (item.getSellPrice() * item.getAmount()) + " coins.");
            } else if (option == 10) {
                ItemUtil.sendItemExamine(player, item);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ITEM_RETRIEVAL_SERVICE;
    }
}
