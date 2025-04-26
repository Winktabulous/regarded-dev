package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.AMULET_OF_DEFENCE
import com.zenyte.game.item.ItemId.AMULET_OF_MAGIC
import com.zenyte.game.item.ItemId.AMULET_OF_POWER
import com.zenyte.game.item.ItemId.AMULET_OF_STRENGTH
import com.zenyte.game.item.ItemId.HOLY_SYMBOL
import com.zenyte.game.model.shop.Shop
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopDiscount
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DavonsAmuletStoreShop : ShopScript() {
    init {
        "Davon's Amulet Store"(101, ShopCurrency.COINS, STOCK_ONLY, Shop.DEFAULT_SELL_MULTIPLIER, ShopDiscount.KARAMJA_DIARY) {
            HOLY_SYMBOL(0, 225, 360)
            AMULET_OF_MAGIC(0, 675, 1080)
            AMULET_OF_DEFENCE(0, 956, 1530)
            AMULET_OF_STRENGTH(0, 1518, 2430)
            AMULET_OF_POWER(0, 2643, 4230)
        }
    }
}