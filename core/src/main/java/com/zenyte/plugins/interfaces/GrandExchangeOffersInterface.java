package com.zenyte.plugins.interfaces;

import com.near_reality.game.model.item.ItemValueExtKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.grandexchange.ExchangeType;
import com.zenyte.game.content.grandexchange.GrandExchange;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.model.ui.testinterfaces.GrandExchangeOffersViewerInterface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 25 nov. 2017 : 19:53:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class GrandExchangeOffersInterface implements UserInterface {
    @Override
    public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
        final boolean buying = player.getVarManager().getBitValue(GrandExchange.TYPE_VARPBIT) == 0;
        final int currentItemId = player.getVarManager().getValue(GrandExchange.ITEM_VARP);
        final GrandExchange exchange = player.getGrandExchange();
        if (interfaceId == GrandExchange.INTERFACE) {
            switch (componentId) {
            case 3:
                exchange.openHistoryInterface();
                break;
            case 4:
                exchange.openOffersInterface();
                break;
            case 6:
                exchange.collectAll(optionId == 1, true);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                final int exchangeSlot = componentId - 7;
                if (slotId == 2) {
                    if (optionId == 1) {
                        exchange.viewOffer(exchangeSlot);
                    } else {
                        exchange.abortOffer(exchangeSlot);
                    }
                } else if (slotId == 3) {
                    exchange.buy(exchangeSlot, true);
                } else if (slotId == 4) {
                    exchange.sell(exchangeSlot, true);
                }
                break;
            case 22:
                exchange.abortOffer();
                return;
            case 23:
                exchange.collectItems(optionId, slotId);
                return;
            case 24:
                if (slotId == 0) {
                    player.sendInputItem("What would you like to " + (buying ? "buy?" : "sell?"), exchange::buy);
                } else if (slotId >= 1 && slotId <= 6) {
                    if (currentItemId <= 0) {
                        return;
                    }
                    int amount = (slotId == 2 || slotId == 3 ? 1 : slotId == 4 ? 10 : slotId == 5 ? 100 : slotId == 6 ? 1000 : -1);
                    final int currentQuantity = exchange.getQuantity();
                    if (!buying) {
                        if (slotId == 6) {
                            //All
                            final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                            final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                            amount = player.getInventory().getAmountOf(otherId) + player.getInventory().getAmountOf(currentItemId);
                        }
                    }
                    if (amount < 1 && slotId != 1) {
                        return;
                    }
                    int quantity = (!buying && slotId == 6) ? amount : currentQuantity == 1 && slotId != 2 && slotId != 3 ? amount : (currentQuantity + amount);
                    if (!buying) {
                        final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                        final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                        final int inInventory = player.getInventory().getAmountOf(otherId) + (otherId == currentItemId ? 0 : player.getInventory().getAmountOf(currentItemId));
                        if (inInventory < quantity) {
                            quantity = inInventory;
                            player.sendMessage("You haven't got enough of that item.");
                        }
                    }
                    if (quantity < 1) {
                        quantity = 1;
                    }
                    exchange.modifyQuantity(quantity);
                } else {
                    if (player.getVarManager().getValue(GrandExchange.ITEM_VARP) != -1) {
                        final int price = exchange.getPrice();
                        switch (slotId) {
                        case 7:
                            player.sendInputInt("How many do you wish to " + (buying ? "buy?" : "sell?"), quantity -> {
                                if (!buying) {
                                    final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                                    final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                                    final int inInventory = player.getInventory().getAmountOf(otherId) + player.getInventory().getAmountOf(currentItemId);
                                    if (inInventory < quantity) {
                                        quantity = inInventory;
                                        player.sendMessage("You haven't got enough of that item.");
                                    }
                                }
                                exchange.modifyQuantity(quantity);
                            });
                            break;
                        case 8:
                            exchange.modifyPrice(price - 1);
                            break;
                        case 9:
                            exchange.modifyPrice(price + 1);
                            break;
                        case 10:
                            exchange.modifyPrice(Math.max(1, Math.min((int) Math.ceil(price * 0.95), price - 1)));
                            break;
                        case 11:
                            exchange.modifyPrice(new Item(player.getVarManager().getValue(GrandExchange.ITEM_VARP)).getSellPrice());
                            break;
                        case 12:
                            player.sendInputInt("Set a price for each item:", exchange::modifyPrice);
                            break;
                        case 13:
                            exchange.modifyPrice(Math.max((int) (price * 1.05), price + 1));
                            break;
                        }
                    }
                }
                break;
            case 27:
                exchange.createOffer();
                break;
            case 30:
                GameInterface.GRAND_EXCHANGE_OFFERS_VIEWER.open(player);
                break;
            }
        } else if (interfaceId == GrandExchange.INVENTORY_INTERFACE) {
            switch (componentId) {
            case 0:
                final Item item = player.getInventory().getItem(slotId);
                if (item == null) {
                    return;
                }
                if (optionId == 10) {
                    ItemUtil.sendItemExamine(player, item);
                    return;
                }
                if (player.getInterfaceHandler().isPresent(GameInterface.GRAND_EXCHANGE_OFFERS_VIEWER)) {
                    if (!item.isTradable()) {
                        player.sendMessage("This item is untradeable.");
                        return;
                    }
                    if (item.getId() == 995 || item.getId() == 13224 || !item.getDefinitions().isGrandExchange()) {
                        player.sendMessage("This item cannot be sold.");
                        return;
                    }
                    final int id = item.getDefinitions().getUnnotedOrDefault();
                    GameInterface.GRAND_EXCHANGE_OFFERS_VIEWER.getPlugin().ifPresent(plugin -> {
                        player.getPacketDispatcher().sendComponentItem(plugin.getInterface().getId(), plugin.getComponent("Item sprite in search"), id, 1);
                        player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Item name"), ItemDefinitions.getOrThrow(id).getName());
                        player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("GE Item price"), StringFormatUtil.format(ItemValueExtKt.getItemValue(id)) + "<br>coins each");
                        player.addTemporaryAttribute("ge_offers_selected_item", id);
                    });
                    GrandExchangeOffersViewerInterface.search(player, id, (ExchangeType) player.getTemporaryAttributes().getOrDefault("ge_offers_selected_exchangetype", ExchangeType.BUYING), null);
                    return;
                }
                if (exchange.sell(item)) {
                    if (player.getVarManager().getBitValue(GrandExchange.SLOT_VARPBIT) == 0) {
                        final int freeSlot = exchange.getFreeSlot();
                        exchange.viewOffer(freeSlot);
                    }
                }
                break;
            }
        }
    }

    @Override
    public int[] getInterfaceIds() {
        return new int[] {GrandExchange.INTERFACE, GrandExchange.INVENTORY_INTERFACE};
    }
}
