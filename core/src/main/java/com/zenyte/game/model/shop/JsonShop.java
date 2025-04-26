package com.zenyte.game.model.shop;

import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 26. sept 2018 : 02:32:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class JsonShop {
	private final String shopName;
	private final int shopUID;
	private final ShopCurrency currency;
	private final ShopPolicy sellPolicy;
	private final ShopDiscount discount;
	private final float sellMultiplier;
	private final List<Item> items;

	public static final int INVALID_SHOP_UID = -1;


	public static final class Item {
		private final int id;
		private final int amount;
		private final int sellPrice;
		private final int buyPrice;
		private final int restockTimer;
		private final boolean ironmanRestricted;

		public Item(int id, int amount, int sellPrice, int buyPrice, int restockTimer, boolean ironmanRestricted) {
			this.id = id;
			this.amount = amount;
			this.sellPrice = sellPrice;
			this.buyPrice = buyPrice;
			this.restockTimer = restockTimer;
			this.ironmanRestricted = ironmanRestricted;
		}

		public int getId() {
			return id;
		}

		public int getAmount() {
			return amount;
		}

		public int getSellPrice() {
			return sellPrice;
		}

		public int getBuyPrice() {
			return buyPrice;
		}

		public int getRestockTimer() {
			return restockTimer;
		}

		public boolean isIronmanRestricted() {
			return ironmanRestricted;
		}
	}

	public JsonShop(String shopName, int shopUID, ShopCurrency currency, ShopPolicy sellPolicy, float sellMultiplier, ShopDiscount discount, List<Item> items) {
		this.shopName = shopName;
		this.shopUID = shopUID;
		this.currency = currency;
		this.sellPolicy = sellPolicy;
		this.sellMultiplier = sellMultiplier;
		this.discount = discount;
		this.items = items;
	}

	public JsonShop(String shopName, ShopCurrency currency, ShopPolicy sellPolicy, float sellMultiplier,
	                ShopDiscount discount, List<Item> items) {
		this(shopName, INVALID_SHOP_UID, currency, sellPolicy, sellMultiplier, discount, items);
	}

	public String getShopName() {
		return shopName;
	}

	public int getShopUID() {
		return shopUID;
	}

	public ShopCurrency getCurrency() {
		return currency;
	}

	public ShopPolicy getSellPolicy() {
		return sellPolicy;
	}

	public ShopDiscount getDiscount() { return discount; }

	public float getSellMultiplier() {
		return sellMultiplier;
	}

	public List<Item> getItems() {
		return items;
	}

	public boolean canEqual(final Object other) {
		return other instanceof JsonShop;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonShop jsonShop = (JsonShop) o;
		return shopUID == jsonShop.shopUID && Float.compare(jsonShop.sellMultiplier, sellMultiplier) == 0 && Objects.equals(shopName, jsonShop.shopName) && currency == jsonShop.currency && sellPolicy == jsonShop.sellPolicy && Objects.equals(items, jsonShop.items);
	}

	@Override
	public int hashCode() {
		return Objects.hash(shopName, shopUID, currency, sellPolicy, sellMultiplier, items);
	}

	@Override
	public String toString() {
		return "JsonShop{" +
				"shopName='" + shopName + '\'' +
				", shopUID=" + shopUID +
				", currency=" + currency +
				", sellPolicy=" + sellPolicy +
				", sellMultiplier=" + sellMultiplier +
				", items=" + items +
				'}';
	}

}
