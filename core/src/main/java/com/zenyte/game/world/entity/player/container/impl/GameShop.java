package com.zenyte.game.world.entity.player.container.impl;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ItemContainer;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.List;

public class GameShop {

    public static final int INTERFACE = 300;
    public static final int INVENTORY_INTERFACE = 301;
    public static final int SIZE = 40;
    private final String name;
    private final int currency;
    private final List<Item> stock;
    private final List<Player> players;
    private boolean generalStore;
    private ItemContainer container;

    public GameShop(final int currency, final String name, final List<Item> stock, final ItemContainer container) {
        this.currency = currency;
        this.name = name;
        this.stock = stock;
        this.container = container;
        players = new ArrayList<>();
    }

    public void openShop(final Player player) {
        player.getTemporaryAttributes().put("Shop", this);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
        player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, INVENTORY_INTERFACE);
        player.getPacketDispatcher().sendClientScript(1074, name, 3);
        player.getPacketDispatcher().sendClientScript(149, 19726336, 93, 4, 7, 0, -1, "Value<col=ff9040>", "Sell 1<col=ff9040>", "Sell 5<col=ff9040>", "Sell 10<col=ff9040>", "Sell 50<col=ff9040>");
		player.getPacketDispatcher().sendComponentSettings(INTERFACE, 16, 0, 41, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2,
				AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
		player.getPacketDispatcher().sendComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2,
				AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
		player.getPacketDispatcher().sendUpdateItemContainer(93, -1, 64209, player.getInventory().getContainer());
		player.setCloseInterfacesEvent(() -> players.remove(player));
		players.add(player);
		refreshStock();
	}

	public void refreshStock() {
		for (final Player player : players) {
			if (player == null) {
				continue;
			}
			player.getPacketDispatcher().sendUpdateItemContainer(3, -1, 64299, container);
		}
	}

	//TODO check if the shop is the recipe of disaster items shop and if the item is barrows gloves
	// if so, update LumbridgeDiary.PURCHASE_BARROWS_GLOVES
	public void buyItem(final Player player, final int slotId, final int amount) {
		final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
		final Item item = container.get(slotId);
		if (item == null) {
			return;
		}
		final int id = item.getId();
		final int itemAmount = item.getAmount();
		if (item.getAmount() <= 0) {
			player.sendMessage("There's currently no stock of this item.");
			return;
		}
		final ItemDefinitions defs = item.getDefinitions();
		final int price = defs.getPrice();

		final Inventory inventory = player.getInventory();
		int quantity = amount;

		/* Sets quantity to item's amount if it's higher than that. */
		if (itemAmount < quantity) {
			quantity = itemAmount;
		}

		/* Special case if the item is stackable. */
		if (defs.isNoted() || defs.isStackable()) {
			/*
			 * If the player already contains one of the stackable item, ensures
			 * that item doesn't overflow Integer.MAX_INTEGER
			 */
			final int inv = inventory.getAmountOf(id);
			if (inv > 0) {
				if (inv + quantity < 0) {
					quantity = Integer.MAX_VALUE - inv;
					if (inv != Integer.MAX_VALUE) {
						player.sendMessage("You need some inventory space to purchase this.");
					}
				}
			} else {
				/*
				 * If player has no free space in inventory, aborts - stackable
				 * items only require one inventory space.
				 */
				if (!inventory.hasFreeSlots()) {
					player.sendMessage("You need some inventory space to purchase this.");
					return;
				}
			}
		} else {
			/*
			 * If the player has less free inventory slots than the requested
			 * quantity, sets the amount to free slots count.
			 */
			if (inventory.getFreeSlots() < quantity) {
				quantity = inventory.getFreeSlots();
				player.sendMessage("You need some inventory space to purchase this.");
			}
		}

		/*
		 * If the quantity is below or equal to zero, aborts and lets the player
		 * know they don't have enough space to purchase it.
		 */
		if (quantity <= 0) {
			player.sendMessage("You need some inventory space to purchase this.");
			return;
		}
		final int totalPrice = price * quantity;

		/*
		 * Sets quantity to how much player can afford if they can't afford the
		 * requested amount.
		 */
		final int invCurrency = inventory.getAmountOf(currency);
		if (totalPrice > invCurrency) {
			quantity = invCurrency / price;
			if (quantity > 0) {
				player.sendMessage("You don't have enough " + ItemDefinitions.get(currency).getName().toLowerCase() + " to purchase this.");
			}
		}

		/*
		 * Lets the player know if they don't have enough cash to purchase any
		 * of the item.
		 */
		if (quantity <= 0) {
			player.sendMessage("You don't have enough " + ItemDefinitions.get(currency).getName().toLowerCase() + " to purchase this.");
			return;
		}
		inventory.deleteItem(currency, price * quantity);
		if (!isBot) {
			if(currency == ItemId.GOLDEN_NUGGET & id == ItemId.COAL_BAG_12019){
				player.getCollectionLog().add(new Item(id, 1));
			}
			inventory.addItem(new Item(id, quantity));
			item.setAmount(itemAmount - quantity);
			refreshStock();
		}
	}

	public void sellItem(final Player player, final int slotId, final int amount) {
		final Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}
		final int id = item.getId();
		final boolean containsItem = container.containsOne(id);

		/**
		 * If the shop is a general store, makes sure there's at least one free
		 * slot in the shop, as there's a 40 item limit.
		 */
		if (generalStore && !containsItem) {
			if (container.freeSlots() <= 0) {
				player.sendMessage("The shop is full!");
				return;
			}
		}

		/**
		 * Makes sure the item being sold isn't coins, or if the shop isn't
		 * general store, ensures that the shop contains that item in stock.
		 */
		if (id == 995 || !generalStore && !containsItem) {
			player.sendMessage("You can't sell this item to this shop.");
			return;
		}
		final ItemDefinitions defs = ItemDefinitions.get(id);
		final Inventory inventory = player.getInventory();
		final int price = defs.getPrice() / 3;
		final int inShop = container.getNumberOf(id);
		int quantity = inventory.getAmountOf(id);

		/**
		 * If requested amount of lower than the quantity, sets the quantity to
		 * the amount requested.
		 */
		if (amount < quantity) {
			quantity = amount;
		}

		/**
		 * If the item overflows Integer.MAX_INTEGER, sets the amount to however
		 * many it can hold.
		 */
		if (inShop + quantity < 0) {
			quantity = Integer.MAX_VALUE - inShop;
			if (inShop != Integer.MAX_VALUE) {
				player.sendMessage("The shop is full!");
			}
		}

		/**
		 * If the shop can't hold any of the item and has reached
		 * Integer.MAX_INTEGER limit, informs the player and aborts.
		 */
		if (quantity <= 0) {
			player.sendMessage("The shop is full!");
			return;
		}

		/**
		 * If the item is stackable, the player has no free inventory slots and
		 * the quantity sold is below the quantity of the item itself, informs
		 * the player they need more free space.
         */
        if (defs.isNoted() || defs.isStackable()) {
            if (!player.getInventory().hasFreeSlots()) {
                if (quantity < item.getAmount()) {
                    player.sendMessage("You need some free inventory space to sell this.");
                    return;
                }
            }
        }

        inventory.deleteItem(new Item(id, quantity));
		final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
		if (!isBot) {
			inventory.addItem(new Item(currency, price * quantity));
			container.add(new Item(id, quantity));
			refreshStock();
		}
	}

    public boolean isGeneralStore() {
        return generalStore;
    }

    public String getName() {
        return name;
    }

    public int getCurrency() {
        return currency;
    }

    public List<Item> getStock() {
        return stock;
    }

    public ItemContainer getContainer() {
        return container;
    }

    public void setContainer(ItemContainer container) {
        this.container = container;
    }

    public List<Player> getPlayers() {
        return players;
    }


}
