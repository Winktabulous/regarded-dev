package com.zenyte.game.world.entity.player.container.impl;

import com.near_reality.game.world.entity.player.container.impl.TradeExtKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.GameConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import kotlinx.datetime.Instant;
import mgi.utilities.StringFormatUtil;
import org.slf4j.event.Level;

import java.util.Optional;

import static com.zenyte.game.world.entity.player.container.impl.TradeStatus.*;

/**
 * @author Tommeh | 15 mei 2018 | 15:49:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class Trade {
    public static final int SIZE = 28;
    public static final int INTERFACE = 335;
    private static final int SECOND_INTERFACE = 334;
    private static final int INVENTORY_INTERFACE = 336;
    private final transient Player player;
    private transient Container container;
    private transient Player partner;
    private transient boolean accepted;

    public Trade(final Player player) {
        this.player = player;
    }

    public void sendTradeRequest(final Player partner) {
        player.sendMessage("Sending trade offer...");
        partner.getPacketDispatcher().sendTradeRequest(player.getPlayerInformation().getDisplayname() + " wishes to trade with you.", player.getPlayerInformation().getDisplayname());
    }

    public void openTradeScreen(final Player partner) {
        this.partner = partner;
        container = new Container(ContainerPolicy.NORMAL, ContainerType.TRADE, Optional.of(player));
        player.stopAll();
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
        player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, INVENTORY_INTERFACE);
        sendDefault();
        refreshTrade(partner);
        refreshMessages(0);
        //player.setCloseInterfacesEvent(() -> closeTrade(TradeStatus.CANCEL));
    }

    private void sendDefault() {
        player.getPacketDispatcher().sendComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentSettings(INTERFACE, 25, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentSettings(INTERFACE, 28, 0, 27, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendClientScript(149, 22020096, 93, 4, 7, 0, -1, "Offer<col=ff9040>", "Offer-5<col=ff9040>", "Offer-10<col=ff9040>", "Offer-All<col=ff9040>", "Offer-X<col=ff9040>");
        player.getPacketDispatcher().sendClientScript(1217, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        player.getPacketDispatcher().sendClientScript(1216, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        partner.getPacketDispatcher().sendComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        partner.getPacketDispatcher().sendComponentSettings(INTERFACE, 25, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        partner.getPacketDispatcher().sendComponentSettings(INTERFACE, 28, 0, 27, AccessMask.CLICK_OP10);
        partner.getPacketDispatcher().sendClientScript(149, 22020096, 93, 4, 7, 0, -1, "Offer<col=ff9040>", "Offer-5<col=ff9040>", "Offer-10<col=ff9040>", "Offer-All<col=ff9040>", "Offer-X<col=ff9040>");
        partner.getPacketDispatcher().sendClientScript(1217, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        partner.getPacketDispatcher().sendClientScript(1216, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    private void refreshMessages(final int stage) {
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 30 : 4, getStatusMessage(stage));
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 31 : 30, stage <= 1 ? "Trading with: " + partner.getPlayerInformation().getDisplayname() : "Trading with:<br>" + partner.getPlayerInformation().getDisplayname());
        if (stage <= 1) {
            player.getPacketDispatcher().sendComponentText(INTERFACE, 9, partner.getPlayerInformation().getDisplayname() + " has " + partner.getInventory().getFreeSlots() + "<br> free inventory slots.");
        }
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 24 : 23, stage <= 1 ? "You offer:<br>(Value: <col=ffffff>" + (isLots(getValue(player)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(player)) + "</col> coins)") : "You are about to give:<br>(Value: <col=ffffff>" + (isLots(getValue(player)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(player)) + " coins)"));
        player.getPacketDispatcher().sendComponentText(stage <= 1 ? INTERFACE : SECOND_INTERFACE, stage <= 1 ? 27 : 24, stage <= 1 ? partner.getPlayerInformation().getDisplayname() + " offers:<br>(Value: <col=ffffff>" + (isLots(getValue(partner)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(partner)) + "</col> coins)") : "In return you will receive:<br>(Value: <col=ffffff>" + (isLots(getValue(player)) ? "Lots!</col>)" : StringFormatUtil.format(getValue(partner)) + " coins)"));
    }

    private String getStatusMessage(final int stage) {
        if (accepted) {
            return "Waiting for other player...";
        } else if (partner.getTrade().isAccepted()) {
            return "Other player has accepted.";
        }
        return stage <= 1 ? "" : "Are you sure you want to make this trade?";
    }

    public void reset() {
        partner = null;
        accepted = false;
    }

    private void refreshTrade(final Player partner) {
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        partner.getPacketDispatcher().sendUpdateItemContainer(90, -2, 0, container);
    }

    public void addItem(final int slot, final int amount) {
        if (!isTrading()) {
            return;
        }
        Item item = player.getInventory().getItem(slot);
        if (item == null) {
            return;
        }
        if (amount < player.getInventory().getAmountOf(item.getId())) {
            item = new Item(item.getId(), amount, item.getAttributes());
        } else {
            item = new Item(item.getId(), player.getInventory().getAmountOf(item.getId()), item.getAttributes());
        }

        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && !this.partner.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && !item.isTradable()) {
            player.sendMessage("You can't trade that.");
            return;
        }
        if(GameConstants.RESTRICTED_TRADE_ITEMS.contains(item.getId())) {
            player.sendMessage("This item has been temporarily restricted.");
            return;
        }
        if(TradeExtKt.canAddItem(this, player, item)) {
            player.log(LogLevel.INFO, "Adding item '" + item + "' into a trade.");
            container.add(item);
            player.getInventory().deleteItem(item);
            refreshTrade(partner);
            refreshMessages(1);
            partner.getTrade().refreshMessages(1);
            cancelAccepted();
        }
    }

    public void removeItem(final int slot, final int amount) {
        if (!isTrading()) {
            return;
        }
        Item item = container.get(slot);
        if (item == null) {
            return;
        }
        if (amount < container.getAmountOf(item.getId())) {
            item = new Item(item.getId(), amount, item.getAttributes());
        } else {
            item = new Item(item.getId(), container.getAmountOf(item.getId()), item.getAttributes());
        }
        player.log(LogLevel.INFO, "Removing item '" + item + "' from a trade.");
        container.remove(item);
        player.getInventory().addItem(item);
        refreshTrade(partner);
        refreshMessages(1);
        partner.getTrade().refreshMessages(1);
        cancelAccepted();
        for (final Integer i : container.getModifiedSlots()) {
            player.getPacketDispatcher().sendClientScript(765, 0, i);
            partner.getPacketDispatcher().sendClientScript(765, 1, i);
        }
    }

    public void accept(final int stage) {
        if (!isTrading()) {
            return;
        } else if (partner.getTrade().isAccepted()) {
            if (stage == 1) {
                for (final Item item : partner.getTrade().getContainer().getItems().values()) {
                    if (player.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                        player.sendMessage("You are holding too many of the same item to continue this trade.");
                        player.getInterfaceHandler().closeInterfaces();
                        return;
                    }
                }
                for (final Item item : container.getItems().values()) {
                    if (partner.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                        partner.sendMessage("You are holding too many of the same item to continue this trade.");
                        partner.getInterfaceHandler().closeInterfaces();
                        return;
                    }
                }
                if (nextStage()) {
                    partner.getTrade().nextStage();
                }
            } else {
                closeTrade(TradeStatus.SUCCESS);
                //player.setCloseInterfacesEvent(null);
                player.getInterfaceHandler().closeInterfaces();
            }
            return;
        }
        accepted = true;
        refreshMessages(stage);
        partner.getTrade().refreshMessages(stage);
    }

    private boolean nextStage() {
        if (!isTrading()) {
            return false;
        } else if (player.getInventory().getContainer().getItems().size() + (SIZE - partner.getTrade().getContainer().getFreeSlotsSize()) > 28) {
            //player.setCloseInterfacesEvent(null);
            closeTrade(TradeStatus.NO_SPACE);
            player.getInterfaceHandler().closeInterfaces();
            return false;
        }
        accepted = false;
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, SECOND_INTERFACE);
        player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        refreshMessages(2);
        partner.getTrade().refreshMessages(2);
        return true;
    }

    public void closeTrade(final TradeStatus stage) {
        if (!isTrading() || !partner.getTrade().isTrading()) {
            return;
        }
        final Player partner = this.partner;
        reset();
        partner.getTrade().reset();
        //partner.setCloseInterfacesEvent(null);
        partner.getInterfaceHandler().closeInterfaces();
        if (!stage.equals(SUCCESS)) {
            player.log(LogLevel.INFO, "Declined trade of items: \nPlayer items: " + container.getItems() + "\nPartner items: " + partner.getTrade().container.getItems());
            for (final Item item : container.getItems().values()) {
                if (item == null) {
                    continue;
                }
                player.getInventory().addOrDrop(item);
            }
            for (final Item item : partner.getTrade().container.getItems().values()) {
                if (item == null) {
                    continue;
                }
                partner.getInventory().addOrDrop(item);
            }
            partner.getTrade().container.clear();
            container.clear();
        } else {
            final ObjectCollection<Item> givenValues = container.getItems().values();
            final ObjectCollection<Item> receivedValues = partner.getTrade().container.getItems().values();
            player.log(LogLevel.INFO, "Accepted trade of items: \nPlayer items: " + container.getItems() + "\nPartner items: " + partner.getTrade().container.getItems());

            for (final Item item : givenValues) {
                if (item == null) continue;
                partner.getInventory().addOrDrop(item);
            }
            for (final Item item : receivedValues) {
                if (item == null) continue;
                player.getInventory().addOrDrop(item);
            }

            final Int2ObjectLinkedOpenHashMap<Item> givenClone = container.getItems().clone();
            final Int2ObjectLinkedOpenHashMap<Item> receivedClone = partner.getTrade().container.getItems().clone();
            // api trade logging

            GameLogger.log(Level.INFO, () -> new GameLogMessage.Trade(
                    Instant.Companion.now(),
                    player.getUsername(),
                    partner.getUsername(),
                    givenClone,
                    receivedClone,
                    player.getLocation()
            ));
            player.sendMessage("Accepted trade.");
            partner.sendMessage("Accepted trade.");
            partner.getTrade().container.clear();
            container.clear();
        }
        if (stage.equals(CANCEL)) {
            player.sendMessage("You decline the trade.");
            partner.sendMessage("Other player declined trade.");
        } else if (stage.equals(NO_SPACE)) {
            player.sendMessage("You don't have enough space in your inventory for this trade.");
            partner.sendMessage("Other player doesn't have enough space in their inventory for this trade.");
            refreshTrade(partner);
            partner.getTrade().container.clear();
            container.clear();
        }
    }

    private void cancelAccepted() {
        if (accepted) {
            accepted = false;
        } else if (partner.getTrade().isAccepted()) {
            partner.getTrade().accepted = false;
        }
        refreshMessages(1);
        partner.getTrade().refreshMessages(1);
    }

    private boolean isTrading() {
        return partner != null;
    }

    private int getValue(final Player player) {
        final Container container = player.getIndex() == this.player.getIndex() ? this.container : partner.getTrade().getContainer();
        if (player == null || container == null) {
            return 0;
        }
        int value = 0;
        for (final Item item : container.getItems().values()) {
            if (item == null || (value == Integer.MAX_VALUE || value < 0)) {
                continue;
            }
            value += item.getAmount() * item.getSellPrice();
        }
        return value;
    }

    private boolean isLots(final int value) {
        return value < 0;
    }

    public Container getContainer() {
        return container;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
